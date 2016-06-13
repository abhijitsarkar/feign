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
import name.abhijitsarkar.feign.core.model.RecordingProperties
import name.abhijitsarkar.feign.core.model.RequestProperties
import name.abhijitsarkar.feign.persistence.IdGenerator
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction
/**
 * @author Abhijit Sarkar
 */
class FeignServiceSpec extends Specification {
    def matchers = [new DefaultPathMatcher(), new DefaultMethodMatcher(), new DefaultQueriesMatcher()
                    , new DefaultHeadersMatcher(), new DefaultBodyMatcher()]

    def feignProperties
    def feignService
    def eventPublisher
    def feignMapping

    def setup() {
        feignService = new FeignService()
        feignProperties = new FeignProperties()
        feignMapping = new FeignMapping()
        feignProperties.mappings = [feignMapping] as List
        feignProperties.recording = new RecordingProperties()

        eventPublisher = Mock(ApplicationEventPublisher)

        feignService.feignProperties = feignProperties
        feignService.matchers = matchers
        feignService.eventPublisher = eventPublisher
    }

    def "finds mapping and publishes request event using given id generator"() {
        setup:
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        def requestProperties = new RequestProperties()
        requestProperties.recording.idGenerator = TestIdGenerator
        feignMapping.request = requestProperties

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        mapping.present
        1 * eventPublisher.publishEvent({
            it.id == '1'
        })
    }

    def "finds mapping and publishes request event using global id generator"() {
        setup:
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        mapping.present
        1 * eventPublisher.publishEvent({
            it.id.startsWith('a-')
        })
    }

    def "does not find mapping but publishes request event using global generator"() {
        setup:
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        feignService.matchers = [new NoMatchMatcher()]

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        !mapping.present
        1 * eventPublisher.publishEvent({
            it.id.startsWith('a-')
        })
    }

    @Unroll
    def "finds mapping and publishes request event if disable is #disable"() {
        setup:
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        def requestProperties = new RequestProperties()
        requestProperties.recording.idGenerator = TestIdGenerator
        requestProperties.recording.disable = disable
        feignMapping.request = requestProperties

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        mapping.present
        if (disable) {
            0 * eventPublisher.publishEvent(_)
        } else {
            1 * eventPublisher.publishEvent(_)
        }

        where:
        disable << [Boolean.FALSE, Boolean.TRUE]
    }

    @Unroll
    def "does not find mapping and publishes request event if global disable is #disable"() {
        setup:
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        feignService.matchers = [new NoMatchMatcher()]
        feignProperties.recording.disable = disable

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        !mapping.present
        if (disable) {
            0 * eventPublisher.publishEvent(_)
        } else {
            1 * eventPublisher.publishEvent(_)
        }

        where:
        disable << [Boolean.FALSE, Boolean.TRUE]
    }
}

class TestIdGenerator implements IdGenerator {
    @Override
    String id(Request request) {
        return '1'
    }
}

class NoMatchMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    Boolean apply(Request request, FeignMapping feignMapping) {
        return Boolean.FALSE
    }
}