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

        Boolean globalIgnoreUnknown = feignMapping.isIgnoreUnknown();

        Boolean localIgnoreUnknown = headers.isIgnoreUnknown();

        Boolean ignoreUnknown = resolveIgnoredProperties(globalIgnoreUnknown, localIgnoreUnknown, Boolean.TRUE);

        if (!isEmpty(requestHeaders) && isEmpty(pairs) && !ignoreUnknown) {
            log.info("{} {}",
                    "Request headers are not empty but request property headers are,",
                    "and ignoreUnknown is false. Headers match returned false.");

            return false;
        }

        Boolean globalIgnoreEmpty = feignMapping.isIgnoreEmpty();
        Boolean localIgnoreEmpty = headers.isIgnoreEmpty();
        Boolean ignoreEmpty = resolveIgnoredProperties(globalIgnoreEmpty, localIgnoreEmpty, Boolean.TRUE);

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
                    Boolean globalIgnoreCase = feignMapping.isIgnoreCase();
                    Boolean localIgnoreCase = headers.isIgnoreCase();
                    Boolean ignoreCase = resolveIgnoredProperties(globalIgnoreCase, localIgnoreCase, Boolean.FALSE);

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

    @SuppressWarnings({"PMD.ConfusingTernary"})
    private boolean resolveIgnoredProperties(Boolean global, Boolean local, Boolean defaultValue) {
        if (local != null) {
            return local;
        } else if (global != null) {
            return global;
        }

        return defaultValue;
    }

}
