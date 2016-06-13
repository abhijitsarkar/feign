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