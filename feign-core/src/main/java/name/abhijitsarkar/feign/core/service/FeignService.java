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
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.Delay;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.FeignProperties;
import name.abhijitsarkar.feign.core.model.RecordingProperties;
import name.abhijitsarkar.feign.core.model.Response;
import name.abhijitsarkar.feign.core.model.ResponseProperties;
import name.abhijitsarkar.feign.persistence.IdGenerator;
import name.abhijitsarkar.feign.persistence.RecordingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
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
public class FeignService {
    @Autowired
    FeignProperties feignProperties;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    Collection<BiFunction<Request, FeignMapping, Boolean>> matchers;

    private final IgnorablePropertiesMerger propertiesMerger = new IgnorablePropertiesMerger();
    private final Map<String, Integer> requestCount = new ConcurrentHashMap<>();

    public Optional<Response> findFeignMapping(Request request) {
        Optional<FeignMapping> feignMapping = feignProperties.getMappings().stream()
                .filter(mapping -> {
                    propertiesMerger.merge(mapping.getRequest(), feignProperties);

                    return matchers.stream()
                            .allMatch(matcher -> matcher.apply(request, mapping));
                })
                .findFirst();

        String id = generateId(request, feignMapping);

        int numRequests = requestCount.merge(id, 1, Math::addExact);
        int numResponse = feignMapping.map(m -> m.getResponse().size()).orElse(0);
        int idx = determineResponseIndex(numRequests, numResponse);

        ResponseProperties rp = numResponse > 0 ? feignMapping.map(m -> m.getResponse().get(idx)).get()
                : new ResponseProperties();

        publishEvent(request, feignMapping, id);

        log.info("Feign mapping {} for path: {}.", feignMapping.isPresent() ? "found" : "not found", request.getPath());

        return feignMapping.map(m -> Response.builder()
                .delayMillis(calculateResponseDelay(id, rp))
                .responseProperties(rp)
                .build());
    }

    long calculateResponseDelay(String id, ResponseProperties rp) {
        Delay delay = rp != null ? rp.getDelay() : null;

        if (delay == null) {
            delay = feignProperties.getDelay();
        }

        int retryCount = requestCount.getOrDefault(id, 1);

        return delay != null ? delay.effectiveDelay(retryCount) : 0l;
    }

    int determineResponseIndex(int numRequests, int numResponse) {
        if (numRequests == 1 || numResponse <= 1) {
            return 0;
        }
        if (numResponse > 0 && numRequests % numResponse == 0) {
            return numResponse - 1;
        }

        return numRequests % numResponse - 1;
    }

    String generateId(Request request, Optional<FeignMapping> feignMapping) {
        Class<? extends IdGenerator> idGenerator = null;

        if (feignMapping.isPresent()) {
            RecordingProperties recording = feignMapping.get().getRequest().getRecording();
            idGenerator = recording.getIdGenerator();
        }

        if (idGenerator == null) {
            idGenerator = feignProperties.getRecording().getIdGenerator();
        }

        Method idMethod = findMethod(idGenerator, "id", Request.class);

        try {
            return (String) invokeMethod(idMethod, idGenerator.newInstance(), request);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(String.format("Failed to generate id for request with path: %s.", request.getPath()), e);
        }
    }

    void publishEvent(Request request, Optional<FeignMapping> feignMapping, String id) {
        RecordingProperties globalRecording = feignProperties.getRecording();

        boolean isDisable = globalRecording.isDisable();

        if (feignMapping.isPresent()) {
            RecordingProperties recording = feignMapping.get().getRequest().getRecording();

            isDisable = recording.isDisable();
        }

        if (!isDisable) {
            eventPublisher.publishEvent(new RecordingRequest(request, id));
        }
    }
}
