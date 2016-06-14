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
import name.abhijitsarkar.feign.core.model.Body
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.FeignProperties
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * @author Abhijit Sarkar
 */
class DefaultBodyMatcherSpec extends Specification {
    @Shared
    def bodyMatcher
    @Shared
    def feignProperties

    def feignMapping
    def requestProperties
    def body

    def setupSpec() {
        bodyMatcher = new DefaultBodyMatcher()

        feignProperties = new FeignProperties()
        feignProperties.postConstruct()
    }

    def setup() {
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
