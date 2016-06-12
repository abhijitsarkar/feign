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

package name.abhijitsarkar.feign.core.service

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.matcher.DefaultBodyMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultHeadersMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultMethodMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultPathMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultQueriesMatcher
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.FeignProperties
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class FeignServiceSpec extends Specification {
    def matchers = [new DefaultPathMatcher(), new DefaultMethodMatcher(), new DefaultQueriesMatcher()
                    , new DefaultHeadersMatcher(), new DefaultBodyMatcher()]
    def feignService
    def eventPublisher = Mock(ApplicationEventPublisher)

    def setup() {
        feignService = new FeignService()
        def feignProperties = new FeignProperties()
        feignProperties.mappings = [new FeignMapping()] as List

        feignService.feignProperties = feignProperties
        feignService.matchers = matchers
        feignService.eventPublisher = eventPublisher
    }

    def "findsFeignMapping and publishes request event"() {
        setup:
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        mapping.present
        1 * eventPublisher.publishEvent(request)
    }
}