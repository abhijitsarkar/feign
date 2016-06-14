/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *  *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 *
 */

package name.abhijitsarkar.feign.core.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.FeignProperties;
import name.abhijitsarkar.feign.core.model.RecordingProperties;
import name.abhijitsarkar.feign.persistence.IdGenerator;
import name.abhijitsarkar.feign.persistence.RecordingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
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

    public Optional<FeignMapping> findFeignMapping(Request request) {
        Optional<FeignMapping> feignMapping = feignProperties.getMappings().stream()
                .filter(mapping -> {
                    propertiesMerger.merge(mapping.getRequest(), feignProperties);

                    return matchers.stream()
                            .allMatch(matcher -> matcher.apply(request, mapping));
                })
                .findFirst();

        publishEvent(request, feignMapping);

        log.info("Feign mapping {} for path: {}.", feignMapping.isPresent() ? "found" : "not found", request.getPath());

        return feignMapping;
    }

    private void publishEvent(Request request, Optional<FeignMapping> feignMapping) {
        RecordingProperties globalRecording = feignProperties.getRecording();

        Class<? extends IdGenerator> idGenerator = null;
        boolean isDisable = globalRecording.isDisable();

        if (feignMapping.isPresent()) {
            RecordingProperties recording = feignMapping.get().getRequest().getRecording();
            idGenerator = recording.getIdGenerator();

            isDisable = recording.isDisable();
        }

        if (!isDisable) {
            if (idGenerator == null) {
                idGenerator = feignProperties.getRecording().getIdGenerator();
            }

            Method idMethod = findMethod(idGenerator, "id", Request.class);
            try {
                String id = (String) invokeMethod(idMethod, idGenerator.newInstance(), request);

                eventPublisher.publishEvent(new RecordingRequest(request, id));
            } catch (ReflectiveOperationException e) {
                log.warn("Failed to generate id for request with path: {}.", request.getPath(), e);
            }
        }
    }
}
