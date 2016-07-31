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

import com.jayway.jsonpath.JsonPath
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

import static org.springframework.http.HttpMethod.GET
/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles('p2')
class FeignSpecP2 extends AbstractFeignSpec {
    def "records request using global id generator"() {
        given:
        def uri = uriBuilder.path('feign/abc').build().toUri()

        when:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, null, String)

        and:
        response = restTemplate.exchange(new URI("http://localhost:$port/requests/feign-1357715445"),
                GET, null, String)

        println(response)

        then:
        JsonPath.read(response.body, '$.path') == '/feign/abc'
    }

    def "records request using given id generator"() {
        given:
        def uri = uriBuilder.path('feign/xyz').build().toUri()

        when:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, null, String)

        and:
        response = restTemplate.exchange(new URI("http://localhost:$port/requests/1"),
                GET, null, String)

        println(response)

        then:
        JsonPath.read(response.body, '$.path') == '/feign/xyz'
    }
}