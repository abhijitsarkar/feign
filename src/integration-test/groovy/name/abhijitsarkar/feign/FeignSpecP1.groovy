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

import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resource
import org.springframework.hateoas.client.Traverson
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.OK

/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles('p1')
class FeignSpecP1 extends AbstractFeignSpec {
    def "matches POST request and finds it"() {
        setup:
        def uri = uriBuilder.path('feign/abc').build().toUri()

        and:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, POST, null, String)

        when:
        assert response.statusCode == OK

        /* https://github.com/spring-projects/spring-hateoas/blob/master/src/test/java/org/springframework/hateoas/client/TraversonTest.java */
        Traverson traverson = new Traverson(new URI("http://localhost:$port"), MediaTypes.HAL_JSON);

        ParameterizedTypeReference<Resource<Request>> type =
                new ParameterizedTypeReference<Resource<Request>>() {};

        def request = traverson.
                follow('requests').
                follow('$._embedded.requests[0]._links.self.href').
                toObject(type)
                .content

        println(request)

        then:
        request.path == '/feign/abc'
        request.method == 'POST'
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