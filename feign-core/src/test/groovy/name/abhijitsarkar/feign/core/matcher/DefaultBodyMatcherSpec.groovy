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

package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.core.model.FeignProperties
import name.abhijitsarkar.feign.model.Request
import name.abhijitsarkar.feign.model.Body
import name.abhijitsarkar.feign.model.FeignMapping
import name.abhijitsarkar.feign.model.RequestProperties
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * @author Abhijit Sarkar
 */
class DefaultBodyMatcherSpec extends Specification {
    @Shared
    def bodyMatcher

    def feignProperties
    def feignMapping
    def requestProperties
    def body

    def setupSpec() {
        bodyMatcher = new DefaultBodyMatcher()
    }

    def setup() {
        feignProperties = new FeignProperties()
        feignProperties.postConstruct()
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties

        body = new Body()
        body.ignoreCase = feignProperties.ignoreCase
        body.ignoreUnknown = feignProperties.ignoreUnknown
        body.ignoreEmpty = feignProperties.ignoreEmpty

        requestProperties.body = body
    }

    def "matches when no request body and empty properties body"() {
        setup:
        body.raw = ''

        def request = Request.builder().build()

        expect:
        bodyMatcher.apply(request, feignMapping)
    }

    @Unroll
    def "match found is #ignoreUnknown when unknown request body and ignoreUnknown is #ignoreUnknown"() {
        setup:
        body.raw = ''
        body.ignoreUnknown = ignoreUnknown

        def request = Request.builder()
                .body('body')
                .build()

        expect:
        bodyMatcher.apply(request, feignMapping) == ignoreUnknown

        where:
        ignoreUnknown << [Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "match found is #ignoreEmpty when no request body, some property body, and ignoreEmpty is #ignoreEmpty"() {
        setup:
        body.raw = 'body'
        body.ignoreEmpty = ignoreEmpty

        def request = Request.builder().build()

        expect:
        bodyMatcher.apply(request, feignMapping) == ignoreEmpty

        where:
        ignoreEmpty << [Boolean.TRUE, Boolean.FALSE]
    }

    def "matches exact content"() {
        setup:
        body.raw = 'body'

        def request = Request.builder()
                .body('body')
                .build()

        expect:
        bodyMatcher.apply(request, feignMapping)
    }

    def "matches regex content"() {
        setup:
        body.raw = 'b.*'

        def request = Request.builder()
                .body('body')
                .build()

        expect:
        bodyMatcher.apply(request, feignMapping)
    }

    @Unroll
    def "match found is #ignoreCase if ignoreCase is #ignoreCase"() {
        setup:
        body.raw = 'body'
        body.ignoreCase = ignoreCase

        def request = Request.builder()
                .body('BODY')
                .build()

        expect:
        bodyMatcher.apply(request, feignMapping) == ignoreCase

        where:
        ignoreCase << [Boolean.TRUE, Boolean.FALSE]
    }
}
