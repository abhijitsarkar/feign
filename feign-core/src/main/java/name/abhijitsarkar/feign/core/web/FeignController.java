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

package name.abhijitsarkar.feign.core.web;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.Body;
import name.abhijitsarkar.feign.core.model.Response;
import name.abhijitsarkar.feign.core.model.ResponseProperties;
import name.abhijitsarkar.feign.core.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
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
    ResponseEntity<?> all(Request request) throws InterruptedException {
        Optional<Response> optionalResponse = feignService.findFeignMapping(request);

        if (optionalResponse.isPresent()) {
            ResponseProperties rp = optionalResponse.get().getResponseProperties();
            long delay = optionalResponse.get().getDelayMillis();

            Thread.sleep(delay);

            HttpHeaders httpHeaders = new HttpHeaders();

            if (!isEmpty(rp.getHeaders())) {
                rp.getHeaders().entrySet()
                        .forEach(e -> httpHeaders.put(e.getKey(), singletonList(e.getValue())));
            }

            Body responseBody = rp.getBody();

            if (!isEmpty(responseBody.toString())) {
                return new ResponseEntity<String>(
                        responseBody.getContent(),
                        httpHeaders,
                        HttpStatus.valueOf(rp.getStatus()));
            }

            return new ResponseEntity<Void>(httpHeaders, HttpStatus.valueOf(rp.getStatus()));
        }

        return new ResponseEntity<Void>(NOT_FOUND);
    }
}
