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
