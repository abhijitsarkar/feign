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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Abhijit Sarkar
 */
@Service
@Slf4j
public class FeignService {
    @Autowired
    FeignProperties feignProperties;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    Collection<BiFunction<Request, FeignMapping, Boolean>> matchers;

    @SuppressWarnings("unchecked")
    public Optional<FeignMapping> findFeignMapping(Request request) {
        Optional<FeignMapping> feignMapping = feignProperties.getMappings().stream()
                .filter(mapping -> {
                    return matchers.stream()
                            .allMatch(matcher -> matcher.apply(request, mapping));
                })
                .findFirst();

        eventPublisher.publishEvent(request);

        log.info("Feign mapping {} for path: {}.", feignMapping.isPresent() ? "found" : "not found", request.getPath());

        return feignMapping;
    }
}
