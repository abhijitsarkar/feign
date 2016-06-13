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

package name.abhijitsarkar.feign.core.matcher;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.RequestProperties;

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
