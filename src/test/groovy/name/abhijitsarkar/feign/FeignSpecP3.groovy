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

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

import static org.springframework.http.HttpMethod.GET

/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles("p3")
class FeignSpecP3 extends AbstractFeignSpec {
    def "matches using AlwaysTrueMatcher"() {
        given:
        def uri = uriBuilder.path('feign/xyz').build().toUri()

        when:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, null, String)

        println(response)

        then:
        response.statusCode == HttpStatus.OK
    }
}