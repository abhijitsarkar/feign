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
import name.abhijitsarkar.feign.core.model.Queries
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * @author Abhijit Sarkar
 */
class DefaultQueriesMatcherSpec extends Specification {
    @Shared
    def queriesMatcher

    def feignProperties
    def queries
    def feignMapping
    def requestProperties

    def setupSpec() {
        queriesMatcher = new DefaultQueriesMatcher()
    }

    def setup() {
        feignProperties = new FeignProperties()
        feignProperties.postConstruct()
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties

        queries = new Queries()
        queries.ignoreCase = feignProperties.ignoreCase
        queries.ignoreUnknown = feignProperties.ignoreUnknown
        queries.ignoreEmpty = feignProperties.ignoreEmpty

        requestProperties.queries = queries
    }

    def "matches when no request params and no properties params"() {
        setup:
        def request = Request.builder().build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }

    @Unroll
    def "match found is #ignoreUnknown when unknown request params and ignoreUnknown is #ignoreUnknown"() {
        setup:
        queries.ignoreUnknown = ignoreUnknown

        def request = Request.builder()
                .queryParams(['a': ['x', 'y'] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping) == ignoreUnknown

        where:
        ignoreUnknown << [Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "match found is #ignoreEmpty when no request params, some property params, and ignoreEmpty is #ignoreEmpty"() {
        setup:
        queries.ignoreEmpty = ignoreEmpty
        queries.pairs = ['a': ['x', 'y'] as List]

        def request = Request.builder().build()

        expect:
        queriesMatcher.apply(request, feignMapping) == ignoreEmpty

        where:
        ignoreEmpty << [Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "matches when a request param and the corresponding property param both have #values values"() {
        setup:
        queries.pairs = ['a': [values] as List]

        def request = Request.builder()
                .queryParams(['a': [values] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)

        where:
        values << [null, ""]
    }

    def "matches when a request param and the corresponding property param both have a null and the same non null values"() {
        setup:
        queries.pairs = ['a': ['b', null] as List]

        def request = Request.builder()
                .queryParams(['a': ['b', null] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }

    def "matches when a request param value is null and the corresponding property param has empty values"() {
        setup:
        queries.pairs = ['a': [] as List]

        def request = Request.builder()
                .queryParams(['a': [null] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }

    def "matches even when the values are unsorted"() {
        setup:
        queries.pairs = ['a': ['b', 'c'] as List]

        def request = Request.builder()
                .queryParams(['a': ['c', 'b'] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }

    @Unroll
    def "match found is #ignoreCase if ignoreCase is #ignoreCase"() {
        setup:
        queries.pairs = ['a': ['b', 'c'] as List]
        queries.ignoreUnknown = false
        queries.ignoreCase = ignoreCase

        def request = Request.builder()
                .queryParams(['a': ['B', 'C'] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping) == ignoreCase

        where:
        ignoreCase << [Boolean.TRUE, Boolean.FALSE]
    }

    def "case insensitive match only applies to values, not param names"() {
        setup:
        queries.pairs = ['a': ['b', 'c'] as List]
        queries.ignoreCase = true
        queries.ignoreUnknown = false

        def request = Request.builder()
                .queryParams(['A': ['B', 'C'] as String[]])
                .build()

        expect:
        !queriesMatcher.apply(request, feignMapping)
    }

    def "matches regex param values"() {
        setup:
        queries.pairs = ['a': ['b.*'] as List]

        def request = Request.builder()
                .queryParams(['A': ['bcd'] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }
}
