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