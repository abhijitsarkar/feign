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
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.Headers;
import name.abhijitsarkar.feign.core.model.RequestProperties;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultHeadersMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    public Boolean apply(Request request, FeignMapping feignMapping) {
        RequestProperties requestProperties = feignMapping.getRequest();
        Headers headers = requestProperties.getHeaders();
        Map<String, String> requestHeaders = request.getHeaders();

        Map<String, String> pairs = headers.getPairs();

        Boolean ignoreUnknown = headers.isIgnoreUnknown();

        if (!isEmpty(requestHeaders) && isEmpty(pairs) && !ignoreUnknown) {
            log.info("{} {}",
                    "Request headers are not empty but request property headers are,",
                    "and ignoreUnknown is false. Headers match returned false.");

            return false;
        }

        Boolean ignoreEmpty = headers.isIgnoreEmpty();

        if (isEmpty(requestHeaders)) {
            boolean match = ignoreEmpty || isEmpty(pairs);

            log.info("Request headers are empty. Headers match returned {}.", match);

            return match;
        }

        boolean match = requestHeaders.entrySet().stream()
                .allMatch(e -> {
                    String key = e.getKey();
                    String valueFromRequest = e.getValue();

                    log.info("Comparing request header {}:[{}] with {}:[{}].",
                            key, valueFromRequest, key, pairs.get(key));

                    String valueFromRequestProperty = nullToEmpty(pairs.get(key));
                    Boolean ignoreCase = headers.isIgnoreCase();

                    if (ignoreCase) {
                        valueFromRequest = isNullOrEmpty(valueFromRequest) ?
                                valueFromRequest : valueFromRequest.toLowerCase(Locale.ENGLISH);
                        valueFromRequestProperty = valueFromRequestProperty.toLowerCase(Locale.ENGLISH);
                    }

                    boolean m1 = valueFromRequest == null && valueFromRequestProperty.isEmpty();
                    boolean m2 = valueFromRequest != null && valueFromRequestProperty.isEmpty() && ignoreUnknown;
                    boolean m3 = valueFromRequest != null && valueFromRequest.matches(valueFromRequestProperty);

                    if (m1) {
                        log.info("Request header value is null and headers are empty.");
                    }
                    if (m2) {
                        log.info("Request header value is not null, headers are empty and ignoreUnknown is true.");
                    }
                    if (m3) {
                        log.info("Match found.");
                    }

                    return m1 || m2 || m3;
                });

        log.info("Headers match returned {}.", match);

        return match;
    }
}
