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

package name.abhijitsarkar.feign.persistence.domain;


import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.IdGenerator;
import name.abhijitsarkar.feign.Request;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultIdGenerator implements IdGenerator {
    @Override
    public String id(Request request) {
        Map<String, String> headers = request.getHeaders();
        String requestId = null;

        if (isEmpty(headers) || StringUtils.isEmpty(headers.get("x-request-id"))) {
            requestId = UUID.randomUUID().toString();
            log.info("Generated random id: {}.", requestId);
        } else {
            requestId = headers.get("x-request-id");
            log.info("Using id from request: {}.", requestId);
        }

        return requestId;
    }
}
