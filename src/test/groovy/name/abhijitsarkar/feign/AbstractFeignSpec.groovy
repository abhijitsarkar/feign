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

import name.abhijitsarkar.feign.core.web.FeignController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
@SpringApplicationConfiguration([FeignApp, FeignConfiguration])
@WebIntegrationTest(randomPort = true)
abstract class AbstractFeignSpec extends Specification {
    @Autowired
    FeignController feignController

    @Value('${local.server.port}')
    protected int port

    protected RestTemplate restTemplate = new TestRestTemplate()

    protected UriComponentsBuilder uriBuilder

//    @PostConstruct
//    def postConstruct() {
//        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:$port")
//    }

    def setup() {
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:$port")
    }
}
