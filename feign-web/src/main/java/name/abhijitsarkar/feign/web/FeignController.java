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
import name.abhijitsarkar.feign.model.Body;
import name.abhijitsarkar.feign.model.Request;
import name.abhijitsarkar.feign.model.Response;
import name.abhijitsarkar.feign.model.ResponseProperties;
import name.abhijitsarkar.feign.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@RestController
@Slf4j
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public class FeignController {
    @Autowired
    FeignService feignService;

    @RequestMapping(path = "/feign/**", produces = APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<String>> all(Request request) {
        Mono<Response> response = feignService.findFeignMapping(request);

        return response.map(r -> {
            ResponseProperties rp = r.getResponseProperties();

            HttpHeaders httpHeaders = new HttpHeaders();

            if (!isEmpty(rp.getHeaders())) {
                rp.getHeaders().entrySet()
                        .forEach(e -> httpHeaders.put(e.getKey(), singletonList(e.getValue())));
            }

            Body responseBody = rp.getBody();

            if (!isEmpty(responseBody.toString())) {
                return ResponseEntity.status(rp.getStatus())
                        .headers(httpHeaders)
                        .body(responseBody.getContent());
            }

            return new ResponseEntity<String>(httpHeaders, HttpStatus.valueOf(rp.getStatus()));
        }).defaultIfEmpty(new ResponseEntity<String>(HttpStatus.NOT_FOUND));
    }
}
