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

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.FeignProperties
import name.abhijitsarkar.feign.core.model.Method
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Shared
import spock.lang.Specification
/**
 * @author Abhijit Sarkar
 */
class DefaultMethodMatcherSpec extends Specification {
    @Shared
    def methodMatcher

    def feignProperties
    def method
    def feignMapping
    def requestProperties

    def setupSpec() {
        methodMatcher = new DefaultMethodMatcher()
    }

    def setup() {
        method = new Method()

        feignProperties = new FeignProperties()
        feignProperties.postConstruct()
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties

        method = new Method()
        method.ignoreCase = feignProperties.ignoreCase

        requestProperties.method = method
    }

    def "matches exact method"() {
        setup:
        method.name = 'GET'
        def request = Request.builder().method('GET').build()

        expect:
        methodMatcher.apply(request, feignMapping)
    }

    def "matches regex method"() {
        setup:
        method.name = '.*'
        def request = Request.builder().method('GET').build()

        expect:
        methodMatcher.apply(request, feignMapping)
    }

    def "matches ignore case"() {
        setup:
        method.name = 'get'
        method.ignoreCase = true
        def request = Request.builder().method('GET').build()

        expect:
        methodMatcher.apply(request, feignMapping)
    }

    def "does not match method"() {
        setup:
        method.name = 'POST'
        def request = Request.builder().method('GET').build()

        expect:
        !methodMatcher.apply(request, feignMapping)
    }
}
