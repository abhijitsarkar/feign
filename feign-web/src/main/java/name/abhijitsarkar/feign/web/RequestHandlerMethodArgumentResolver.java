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

package name.abhijitsarkar.feign.web;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.model.Request;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.result.method.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toMap;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class RequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Request.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter param,
                                        BindingContext bindingContext, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String body = null;

        try {
            body = request.getBody()
                    .map(DataBuffer::asByteBuffer)
                    .map(ByteBuffer::toString)
                    .singleOrEmpty()
                    .blockMillis(3000);
        } catch (RuntimeException e) {
            log.warn("Timeout reading body.");
        }

        Map<String, String[]> queryParams = request.getQueryParams().entrySet().stream()
                .collect(toMap(Entry::getKey, e -> e.getValue().toArray(new String[]{})));

        return Mono.just(
                Request.builder()
                        .path(request.getURI().getPath())
                        .method(request.getMethod().name())
                        .queryParams(queryParams)
                        .headers(request.getHeaders().toSingleValueMap())
                        .body(body)
                        .build()
        );
    }
}
