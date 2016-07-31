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

package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.core.model.Body
import name.abhijitsarkar.feign.core.model.Response
import name.abhijitsarkar.feign.core.model.ResponseProperties
import name.abhijitsarkar.feign.core.service.FeignService
import org.springframework.http.HttpStatus
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class FeignControllerSpec extends Specification {
    FeignController feignController
    def feignService
    def responseProperties

    def setup() {
        feignController = new FeignController()
        feignService = Mock(FeignService)
        feignController.feignService = feignService

        responseProperties = new ResponseProperties()
    }

    def "returns 404 response if no mapping found"() {
        setup:
        feignService.findFeignMapping(_) >> Optional.empty()

        when:
        def retVal = feignController.all(null)

        then:
        retVal.getStatusCode() == HttpStatus.NOT_FOUND
    }

    def "returns response with status specified by mapping"() {
        setup:
        responseProperties.status = HttpStatus.OK.value()

        feignService.findFeignMapping(_) >> Optional.of(Response.builder()
                .responseProperties(responseProperties)
                .build())

        when:
        def retVal = feignController.all(null)

        then:
        retVal.getStatusCode().value() == responseProperties.status
    }

    def "returns headers if specified by mapping"() {
        setup:
        responseProperties.headers = ['a': 'b']

        feignService.findFeignMapping(_) >> Optional.of(Response.builder()
                .responseProperties(responseProperties)
                .build())

        when:
        def retVal = feignController.all(null)

        then:
        retVal.headers.toSingleValueMap() == responseProperties.headers
    }

    def "returns body if specified by mapping"() {
        setup:
        def body = new Body()
        body.raw = 'body'
        responseProperties.body = body

        feignService.findFeignMapping(_) >> Optional.of(Response.builder()
                .responseProperties(responseProperties)
                .build())

        when:
        def retVal = feignController.all(null)

        then:
        retVal.body == 'body'
    }
}