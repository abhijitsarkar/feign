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
import name.abhijitsarkar.feign.core.model.Headers
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * @author Abhijit Sarkar
 */
class DefaultHeadersMatcherSpec extends Specification {
    @Shared
    def headersMatcher

    def feignProperties
    def headers
    def feignMapping
    def requestProperties

    def setupSpec() {
        headersMatcher = new DefaultHeadersMatcher()
    }

    def setup() {
        feignProperties = new FeignProperties()
        feignProperties.postConstruct()
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties

        headers = new Headers()
        headers.ignoreCase = feignProperties.ignoreCase
        headers.ignoreUnknown = feignProperties.ignoreUnknown
        headers.ignoreEmpty = feignProperties.ignoreEmpty

        requestProperties.headers = headers
    }

    def "matches when no request headers and no properties headers"() {
        setup:
        def request = Request.builder().build()

        expect:
        headersMatcher.apply(request, feignMapping)
    }

    @Unroll
    def "match found is #ignoreUnknown when unknown request headers and ignoreUnknown is #ignoreUnknown"() {
        setup:
        headers.ignoreUnknown = ignoreUnknown

        def request = Request.builder()
                .headers(['a': 'b'])
                .build()

        expect:
        headersMatcher.apply(request, feignMapping) == ignoreUnknown

        where:
        ignoreUnknown << [Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "match found is #ignoreEmpty when no request headers, some property headers, and ignoreEmpty is #ignoreEmpty"() {
        setup:
        headers.ignoreEmpty = ignoreEmpty
        headers.pairs = ['a': 'b']

        def request = Request.builder().build()

        expect:
        headersMatcher.apply(request, feignMapping) == ignoreEmpty

        where:
        ignoreEmpty << [Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "matches when a request header and the corresponding property header both have #values values"() {
        setup:
        headers.pairs = ['a': value]

        def request = Request.builder()
                .headers(['a': value])
                .build()

        expect:
        headersMatcher.apply(request, feignMapping)

        where:
        value << [null, ""]
    }

    @Unroll
    def "match found is #ignoreCase if ignoreCase is #ignoreCase"() {
        setup:
        headers.pairs = ['a': 'B']
        headers.ignoreCase = ignoreCase
        headers.ignoreUnknown = false

        def request = Request.builder()
                .headers(['a': 'b'])
                .build()

        expect:
        headersMatcher.apply(request, feignMapping) == ignoreCase

        where:
        ignoreCase << [Boolean.TRUE, Boolean.FALSE]
    }

    def "case insensitive match only applies to values, not header names"() {
        setup:
        headers.pairs = ['a': 'b']
        headers.ignoreUnknown = false
        headers.ignoreCase = true

        def request = Request.builder()
                .headers(['A': 'b'])
                .build()

        expect:
        !headersMatcher.apply(request, feignMapping)
    }

    def "matches regex header value"() {
        setup:
        headers.pairs = ['a': 'b.*']

        def request = Request.builder()
                .headers(['A': 'bcd'])
                .build()

        expect:
        headersMatcher.apply(request, feignMapping)
    }
}
