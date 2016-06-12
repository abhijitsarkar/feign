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

package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.core.model.Body
import name.abhijitsarkar.feign.core.model.FeignMapping
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
    def feignMapping
    def responseProperties

    def setup() {
        feignController = new FeignController()
        feignService = Mock(FeignService)
        feignController.feignService = feignService

        feignMapping = new FeignMapping()
        responseProperties = new ResponseProperties()
        feignMapping.response = responseProperties
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

        feignService.findFeignMapping(_) >> Optional.of(feignMapping)

        when:
        def retVal = feignController.all(null)

        then:
        retVal.getStatusCode().value() == responseProperties.status
    }

    def "returns headers if specified by mapping"() {
        setup:
        responseProperties.headers = ['a': 'b']

        feignService.findFeignMapping(_) >> Optional.of(feignMapping)

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

        feignService.findFeignMapping(_) >> Optional.of(feignMapping)

        when:
        def retVal = feignController.all(null)

        then:
        retVal.body == 'body'
    }
}