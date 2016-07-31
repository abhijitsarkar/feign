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
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.Body;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.RequestProperties;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.google.common.base.Strings.nullToEmpty;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultBodyMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    public Boolean apply(Request request, FeignMapping feignMapping) {
        RequestProperties requestProperties = feignMapping.getRequest();
        Body body = requestProperties.getBody();
        String requestBody = request.getBody();

        Boolean ignoreUnknown = body.isIgnoreUnknown();
        Boolean ignoreEmpty = body.isIgnoreEmpty();

        String content = nullToEmpty(body.getContent());

        if ((isEmpty(requestBody) && isEmpty(content))
                || (isEmpty(requestBody) && ignoreEmpty)
                || (!isEmpty(requestBody) && isEmpty(content) && ignoreUnknown)) {
            return true;
        } else if (isEmpty(requestBody)) {
            return false;
        }

        Boolean ignoreCase = body.isIgnoreCase();
        boolean match = ignoreCase ? requestBody.toLowerCase(Locale.ENGLISH).matches(content.toLowerCase(Locale.ENGLISH))
                : requestBody.matches(content);

        log.info("Comparing request body: {} with: {}.", requestBody, content);
        log.info("Body match returned: {}.", match);

        return match;
    }
}
