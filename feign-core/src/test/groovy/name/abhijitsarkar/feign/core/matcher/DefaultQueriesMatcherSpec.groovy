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
import name.abhijitsarkar.feign.core.model.Queries
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class DefaultQueriesMatcherSpec extends Specification {
    def queriesMatcher = new DefaultQueriesMatcher()
    def feignMapping
    def requestProperties

    def setup() {
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties
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
        def queries = new Queries()
        queries.ignoreUnknown = ignoreUnknown
        requestProperties.queries = queries

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
        def queries = new Queries()
        queries.ignoreEmpty = ignoreEmpty
        queries.pairs = ['a': ['x', 'y'] as List]
        requestProperties.queries = queries

        def request = Request.builder().build()

        expect:
        queriesMatcher.apply(request, feignMapping) == ignoreEmpty

        where:
        ignoreEmpty << [Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "matches when a request param and the corresponding property param both have #values values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': [values] as List]
        requestProperties.queries = queries

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
        def queries = new Queries()
        queries.pairs = ['a': ['b', null] as List]
        requestProperties.queries = queries

        def request = Request.builder()
                .queryParams(['a': ['b', null] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }

    def "matches when a request param value is null and the corresponding property param has empty values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': [] as List]
        requestProperties.queries = queries

        def request = Request.builder()
                .queryParams(['a': [null] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }

    def "matches even when the values are unsorted"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b', 'c'] as List]
        requestProperties.queries = queries

        def request = Request.builder()
                .queryParams(['a': ['c', 'b'] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }

    @Unroll
    def "match found is #ignoreCase if ignoreCase is #ignoreCase"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b', 'c'] as List]
        queries.ignoreUnknown = false
        queries.ignoreCase = ignoreCase
        requestProperties.queries = queries

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
        def queries = new Queries()
        queries.pairs = ['a': ['b', 'c'] as List]
        queries.ignoreCase = true
        queries.ignoreUnknown = false
        requestProperties.queries = queries

        def request = Request.builder()
                .queryParams(['A': ['B', 'C'] as String[]])
                .build()

        expect:
        !queriesMatcher.apply(request, feignMapping)
    }

    def "matches regex param values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b.*'] as List]
        requestProperties.queries = queries

        def request = Request.builder()
                .queryParams(['A': ['bcd'] as String[]])
                .build()

        expect:
        queriesMatcher.apply(request, feignMapping)
    }
}
