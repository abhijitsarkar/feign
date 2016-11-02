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
import name.abhijitsarkar.feign.model.FeignMapping
import name.abhijitsarkar.feign.model.Path
import name.abhijitsarkar.feign.model.RequestProperties
import spock.lang.Shared
import spock.lang.Specification
/**
 * @author Abhijit Sarkar
 */
class DefaultPathMatcherSpec extends Specification {
    @Shared
    def pathMatcher

    def feignProperties
    def path
    def feignMapping
    def requestProperties

    def setupSpec() {
        pathMatcher = new DefaultPathMatcher()
    }

    def setup() {
        feignProperties = new FeignProperties()
        feignProperties.postConstruct()
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties

        path = new Path()
        path.ignoreCase = feignProperties.ignoreCase

        requestProperties.path = path
    }

    def "matches exact path"() {
        setup:
        path.uri = '/abc/xyz'
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "matches regex path"() {
        setup:
        path.uri = '/abc/**'
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "matches ignore case"() {
        setup:
        path.uri = '/abc/xyz'
        path.ignoreCase = true
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "does not match path"() {
        setup:
        path.uri = '/abc'
        def request = Request.builder().path('/xyz').build()

        expect:
        !pathMatcher.apply(request, feignMapping)
    }
}
