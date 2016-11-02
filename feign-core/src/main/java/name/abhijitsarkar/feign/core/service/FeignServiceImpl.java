/*
 * Copyright (c) 2016, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * License for more details.
 */

package name.abhijitsarkar.feign.core.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.core.model.FeignProperties;
import name.abhijitsarkar.feign.model.*;
import name.abhijitsarkar.feign.persistence.IdGenerator;
import name.abhijitsarkar.feign.persistence.RecordingRequest;
import name.abhijitsarkar.feign.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

/**
 * @author Abhijit Sarkar
 */
@Service
@Slf4j
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public class FeignServiceImpl implements FeignService {
    @Autowired
    FeignProperties feignProperties;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    Collection<BiFunction<Request, FeignMapping, Boolean>> matchers;

    private final IgnorablePropertiesMerger propertiesMerger = new IgnorablePropertiesMerger();
    private final Map<String, Integer> requestCount = new ConcurrentHashMap<>();

    @Override
    public Mono<Response> findFeignMapping(Request request) {
        Mono<FeignMapping> feignMapping = Mono.just(feignProperties)
                .flatMap(fp -> Flux.fromIterable(fp.getMappings()))
                .filter(mapping -> {
                    propertiesMerger.merge(mapping.getRequest(), feignProperties);

                    return matchers.stream()
                            .allMatch(matcher -> matcher.apply(request, mapping));
                })
                .singleOrEmpty()
                .doOnSuccess(m -> log.info("Feign mapping for path: {} is: {}.", request.getPath(), m))
                .doOnError(t -> log.error("Failed to find Feign mapping for path: {}.", request.getPath(), t));

        Mono<String> id = generateId(request, feignMapping);
        Mono<Tuple2<Boolean, String>> publishResult = publishEvent(feignMapping, id, request);

        Mono<Integer> numRequests = id.map(i -> requestCount.merge(i, 1, Math::addExact));
        Mono<Integer> numResponse = feignMapping.map(m -> m.getResponse().size()).defaultIfEmpty(0);
        Mono<Integer> idx = numRequests.and(numResponse).map(this::determineResponseIndex);
        Mono<ResponseProperties> rp = responseProperties(feignMapping, numResponse, idx);

        Mono<Long> delayInMillis = publishResult
                .map(Tuple2::getT2)
                .and(rp)
                .map(this::calculateResponseDelayInMillis);

        return rp
                .flatMap(p -> {
                    Response response = new Response(p);
                    return delayInMillis
                            .flatMap(d -> Mono.just(response).delaySubscription(Duration.ofMillis(d)));
                })
                .singleOrEmpty()
                .and(feignMapping)
                .map(Tuple2::getT1)
                .doOnSuccess(r -> log.info("Response: {}.", r))
                .doOnError(t -> log.error("Failed to build response.", t));
    }

    Mono<ResponseProperties> responseProperties(
            Mono<FeignMapping> feignMapping, Mono<Integer> numResponse, Mono<Integer> idx) {
        return numResponse.and(idx)
                .flatMap(t -> t.getT1() > 0 ?
                        feignMapping.map(m -> m.getResponse().get(t.getT2()))
                        : Mono.fromSupplier(ResponseProperties::new))
                .singleOrEmpty()
                .doOnSuccess(r -> log.info("Response properties: {}.", r))
                .doOnError(t -> log.error("Failed to find response properties.", t));
    }

    Mono<Tuple2<Boolean, String>> publishEvent(Mono<FeignMapping> feignMapping, Mono<String> id, Request request) {
        return feignMapping
                .map(m -> Optional.ofNullable(m.getRequest().getRecording().isDisable())
                        .orElse(feignProperties.getRecording().isDisable())
                        .booleanValue()
                )
                .defaultIfEmpty(feignProperties.getRecording().isDisable())
                .and(id)
                .doOnSuccess(t -> {
                    boolean recordingEnabled = !t.getT1();
                    String requestId = t.getT2();
                    log.info("Recording disabled: {}, Request Id: {}.", !recordingEnabled, requestId);
                    if (recordingEnabled) {
                        eventPublisher.publishEvent(new RecordingRequest(request, requestId));
                    }
                })
                .doOnError(t -> log.error("Failed to publish recording event.", t));
    }


    Mono<String> generateId(Request request, Mono<FeignMapping> feignMapping) {
        return feignMapping
                .map(m -> { // must not return null
                    Class<? extends IdGenerator> idGenerator = m.getRequest().getRecording().getIdGenerator();
                    return idGenerator == null ? feignProperties.getRecording().getIdGenerator() : idGenerator;
                })
                .defaultIfEmpty(feignProperties.getRecording().getIdGenerator())
                .map(idGenerator -> {
                    Method idMethod = findMethod(idGenerator, "id", Request.class);
                    try {
                        return (String) invokeMethod(idMethod, idGenerator.newInstance(), request);
                    } catch (ReflectiveOperationException ex) {
                        throw new RuntimeException(String.format("Failed to generate id for request with path: %s.",
                                request.getPath()), ex);
                    }
                })
                .doOnSuccess(id -> log.info("Request Id: {}.", id))
                .doOnError(t -> log.error("Failed to generate request id.", t));
    }

    long calculateResponseDelayInMillis(Tuple2<String, ResponseProperties> t) {
        String id = t.getT1();
        ResponseProperties rp = t.getT2();

        Delay delay = rp != null ? rp.getDelay() : null;

        if (delay == null) {
            delay = feignProperties.getDelay();
        }

        int retryCount = requestCount.getOrDefault(id, 1);

        return delay != null ? delay.effectiveDelay(retryCount) : 0l;
    }

    int determineResponseIndex(Tuple2<Integer, Integer> t) {
        int numRequests = t.getT1();
        int numResponse = t.getT2();

        if (numRequests == 1 || numResponse <= 1) {
            return 0;
        }
        if (numResponse > 0 && numRequests % numResponse == 0) {
            return numResponse - 1;
        }

        return numRequests % numResponse - 1;
    }
}
