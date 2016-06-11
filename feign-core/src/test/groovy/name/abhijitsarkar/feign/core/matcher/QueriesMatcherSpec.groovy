package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.core.model.Queries
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class QueriesMatcherSpec extends Specification {
    def queriesMatcher
    def requestProperties

    def setup() {
        requestProperties = new RequestProperties()
    }

    def "matches when no request params and no properties params"() {
        setup:
        queriesMatcher = new QueriesMatcher([:])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "does not match when unknown request params and ignoreUnknown is false"() {
        setup:
        def queries = new Queries()
        queries.ignoreUnknown = false
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': ['x', 'y'] as String[]])

        expect:
        !queriesMatcher.test(requestProperties)
    }

    def "matches when unknown request params and ignoreUnknown is true"() {
        setup:
        queriesMatcher = new QueriesMatcher(['a': ['x', 'y'] as String[]])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "does not match when no request params, some properties params and ignoreEmpty is false"() {
        setup:
        def queries = new Queries()
        queries.ignoreEmpty = false
        queries.pairs = ['a': ['x', 'y'] as List]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher([:])

        expect:
        !queriesMatcher.test(requestProperties)
    }

    def "matches when no request params, some properties params and ignoreEmpty is true"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['x', 'y'] as List]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher([:])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "matches when a request param and the corresponding property param both have null values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': null]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': null])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "matches when a request param and the corresponding property param both have empty values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': [] as List]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': [] as String[]])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "matches when a request param and the corresponding property param both have a null and the same non null values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b', null] as List]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': ['b', null] as String[]])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "matches when a request param value is null and the corresponding property param has empty values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': [] as List]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': [null] as String[]])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "matches even when the values are unsorted"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b', 'c'] as List]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': ['c', 'b'] as String[]])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "matches case insensitive if ignoreCase is true"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b', 'c'] as List]
        queries.ignoreCase = true
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': ['B', 'C'] as String[]])

        expect:
        queriesMatcher.test(requestProperties)
    }

    def "case insensitive match only applies to values, not param names"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b', 'c'] as List]
        queries.ignoreCase = true
        queries.ignoreUnknown = false
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['A': ['b', 'c'] as String[]])

        expect:
        !queriesMatcher.test(requestProperties)
    }

    def "matches regex param values"() {
        setup:
        def queries = new Queries()
        queries.pairs = ['a': ['b.*'] as List]
        requestProperties.queries = queries

        queriesMatcher = new QueriesMatcher(['a': ['bcd'] as String[]])

        expect:
        queriesMatcher.test(requestProperties)
    }
}
