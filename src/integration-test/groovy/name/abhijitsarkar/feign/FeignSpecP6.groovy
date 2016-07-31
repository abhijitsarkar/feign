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

import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeoutException

import static java.util.concurrent.TimeUnit.SECONDS
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.OK

/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles('p6')
class FeignSpecP6 extends AbstractFeignSpec {
    def "delays the response as per global policy"() {
        setup:
        def uri = uriBuilder.path('feign/abc').build().toUri()

        when:
        CompletableFuture<ResponseEntity<String>> future =
                CompletableFuture.supplyAsync { restTemplate.exchange(uri, POST, null, String) }
        future.get(1, SECONDS)

        then:
        thrown TimeoutException
    }

    def "delays the response as per local policy"() {
        setup:
        def uri = uriBuilder.path('feign/xyz').build().toUri()

        when:
        CompletableFuture<ResponseEntity<String>> future =
                CompletableFuture.supplyAsync { restTemplate.exchange(uri, POST, null, String) }
        ResponseEntity<String> response = future.get(2, SECONDS)

        then:
        response.statusCode == OK
    }
}