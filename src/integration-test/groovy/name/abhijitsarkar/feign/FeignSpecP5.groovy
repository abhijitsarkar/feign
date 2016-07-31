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

import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles('p5')
class FeignSpecP5 extends AbstractFeignSpec {
    def "returns the expected response when there are many"() {
        setup:
        def uri = uriBuilder.path('feign/abc').build().toUri()

        when:
        def ResponseEntity<String> response1 =
                restTemplate.exchange(uri, POST, null, String)
        def ResponseEntity<String> response2 =
                restTemplate.exchange(uri, POST, null, String)
        def ResponseEntity<String> response3 =
                restTemplate.exchange(uri, POST, null, String)

        then:
        response1.statusCode == OK
        response1.body == 'body1'

        response2.statusCode == OK
        response2.body == 'body2'

        response3.statusCode == INTERNAL_SERVER_ERROR
        !response3.body
    }
}