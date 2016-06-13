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
import name.abhijitsarkar.feign.IdGenerator;
import name.abhijitsarkar.feign.RecordingRequest;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.FeignProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;
import static org.springframework.util.StringUtils.isEmpty;

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

    @Autowired(required = false)
    IdGenerator idGenerator;

    public Optional<FeignMapping> findFeignMapping(Request request) {
        Optional<FeignMapping> feignMapping = feignProperties.getMappings().stream()
                .filter(mapping -> {
                    return matchers.stream()
                            .allMatch(matcher -> matcher.apply(request, mapping));
                })
                .findFirst();

        publishEvent(request, feignMapping);

        log.info("Feign mapping {} for path: {}.", feignMapping.isPresent() ? "found" : "not found", request.getPath());

        return feignMapping;
    }

    @SuppressWarnings({"PMD.ConfusingTernary"})
    private void publishEvent(Request request, Optional<FeignMapping> feignMapping) {
        String id = null;

        Class<? extends IdGenerator> clazz = feignMapping.map(m -> m.getRequest().getIdGenerator())
                .filter(Objects::nonNull)
                .orElse(null);

        if (clazz != null) {
            Method idMethod = findMethod(clazz, "id", Request.class);
            try {
                id = (String) invokeMethod(idMethod, clazz.newInstance(), request);
            } catch (ReflectiveOperationException e) {
                log.warn("Failed to generate id for request with path: {}.", request.getPath(), e);
            }
        } else if (idGenerator != null) {
            id = idGenerator.id(request);
        }

        if (!isEmpty(id)) {
            eventPublisher.publishEvent(new RecordingRequest(request, id));
        } else {
            log.warn("Failed to record request with path: {}, could not generate id.", request.getPath());
        }
    }
}
