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

package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.FeignProperties
import name.abhijitsarkar.feign.core.model.Path
import name.abhijitsarkar.feign.core.model.RequestProperties
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
