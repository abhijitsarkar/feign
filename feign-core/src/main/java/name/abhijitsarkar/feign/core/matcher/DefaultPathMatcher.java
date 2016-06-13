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
import org.springframework.util.AntPathMatcher;

import java.util.Locale;
import java.util.function.BiFunction;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultPathMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    private final transient org.springframework.util.PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Boolean apply(Request request, FeignMapping feignMapping) {
        RequestProperties requestProperties = feignMapping.getRequest();
        String requestPath = request.getPath();

        String path = requestProperties.getPath().getUri();
        boolean ignoreCase = requestProperties.getPath().isIgnoreCase();

        boolean match = ignoreCase ? pathMatcher.match(path.toLowerCase(Locale.ENGLISH),
                requestPath.toLowerCase(Locale.ENGLISH)) :
                pathMatcher.match(path, requestPath);

        log.info("Comparing request path: {} with uri: {}.", requestPath, path);
        log.info("Path match returned: {}.", match);

        return match;
    }
}
