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

package name.abhijitsarkar.feign.core.matcher;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.model.Request;
import name.abhijitsarkar.feign.model.FeignMapping;
import name.abhijitsarkar.feign.model.RequestProperties;

import java.util.Locale;
import java.util.function.BiFunction;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultMethodMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    public Boolean apply(Request request, FeignMapping feignMapping) {
        RequestProperties requestProperties = feignMapping.getRequest();
        String requestMethod = request.getMethod();

        String method = requestProperties.getMethod().getName();
        boolean ignoreCase = requestProperties.getMethod().isIgnoreCase();

        boolean match = ignoreCase ? requestMethod.toLowerCase(Locale.ENGLISH).matches(method.toLowerCase(Locale.ENGLISH)) :
                requestMethod.matches(method);

        log.info("Comparing request method: {} with: {}.", requestMethod, method);
        log.info("Method match returned {}.", match);

        return match;
    }
}
