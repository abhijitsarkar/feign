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

package name.abhijitsarkar.feign

import name.abhijitsarkar.feign.model.Request
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resource
import org.springframework.hateoas.client.Traverson
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.reactive.ClientRequest
import org.springframework.web.client.reactive.ClientResponse
import reactor.core.publisher.Mono

import java.util.function.Function

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles('p1')
class FeignSpecP1 extends AbstractFeignSpec {
    def "matches POST request and finds it"() {
        setup:
        ClientRequest<Void> request1 = ClientRequest.POST("http://localhost:{port}/feign/abc", port)
                .build();

        and:
        def response = webClient.exchange(request1)
        Function<ClientResponse, Mono<HttpStatus>> fn = { r -> Mono.just(r.statusCode())}
        def statusCode = response.then(fn)
                .block()

        when:
        assert statusCode == OK

        /* https://github.com/spring-projects/spring-hateoas/blob/master/src/test/java/org/springframework/hateoas/client/TraversonTest.java */
        Traverson traverson = new Traverson(new URI("http://localhost:$port"), MediaTypes.HAL_JSON);

        ParameterizedTypeReference<Resource<Request>> type =
                new ParameterizedTypeReference<Resource<Request>>() {};

        def request2 = traverson.
                follow('requests').
                follow('$._embedded.requests[0]._links.self.href').
                toObject(type)
                .content

        println(request2)

        then:
        request2.path == '/feign/abc'
        request2.method == 'POST'
    }

    def "matches any GET request"() {
        setup:
        def uri = uriBuilder.path('feign/xyz').build().toUri()

        when:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, null, String)

        then:
        response.statusCode == OK
    }
}