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
import name.abhijitsarkar.feign.core.model.Path
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class DefaultPathMatcherSpec extends Specification {
    def pathMatcher = new DefaultPathMatcher()
    def feignMapping
    def requestProperties

    def setup() {
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties
    }

    def "matches exact path"() {
        setup:
        def path = new Path()
        path.uri = '/abc/xyz'
        requestProperties.path = path
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "matches regex path"() {
        setup:
        def path = new Path()
        path.uri = '/abc/**'
        requestProperties.path = path
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "matches ignore case"() {
        setup:
        def path = new Path()
        path.uri = '/abc/xyz'
        path.ignoreCase = true
        requestProperties.path = path
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "does not match path"() {
        setup:
        def path = new Path()
        path.uri = '/abc'
        requestProperties.path = path
        def request = Request.builder().path('/xyz').build()

        expect:
        !pathMatcher.apply(request, feignMapping)
    }
}
