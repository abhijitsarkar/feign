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

import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.client.reactive.WebClient
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

/**
 * @author Abhijit Sarkar
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [FeignApp])
abstract class AbstractFeignSpec extends Specification {
    @LocalServerPort
    int port;

    WebClient webClient;

    def setup() {
        this.webClient = WebClient.create(new ReactorClientHttpConnector())
    }
}
