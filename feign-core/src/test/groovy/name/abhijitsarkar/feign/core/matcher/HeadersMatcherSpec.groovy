package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.core.model.Headers
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class HeadersMatcherSpec extends Specification {
    def headersMatcher
    def requestProperties

    def setup() {
        requestProperties = new RequestProperties()
    }

    def "matches when no request headers and no properties headers"() {
        setup:
        headersMatcher = new HeadersMatcher([:])

        expect:
        headersMatcher.test(requestProperties)
    }

    def "does not match when unknown request headers and ignoreUnknown is false"() {
        setup:
        def headers = new Headers()
        headers.ignoreUnknown = false
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher(['a': 'b'])

        expect:
        !headersMatcher.test(requestProperties)
    }

    def "matches when unknown request headers and ignoreUnknown is true"() {
        setup:
        headersMatcher = new HeadersMatcher(['a': 'b'])

        expect:
        headersMatcher.test(requestProperties)
    }

    def "does not match when no request headers, some properties headers and ignoreEmpty is false"() {
        setup:
        def headers = new Headers()
        headers.ignoreEmpty = false
        headers.pairs = ['a': 'x']
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher([:])

        expect:
        !headersMatcher.test(requestProperties)
    }

    def "matches when no request headers, some properties headers and ignoreEmpty is true"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': 'x']
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher([:])

        expect:
        headersMatcher.test(requestProperties)
    }

    def "matches when a request header and the corresponding property header both have null values"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': null]
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher(['a': null])

        expect:
        headersMatcher.test(requestProperties)
    }

    def "matches when a request headers and the corresponding property headers both have empty values"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': ""]
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher(['a': ""])

        expect:
        headersMatcher.test(requestProperties)
    }

    def "matches case insensitive if ignoreCase is true"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': 'B']
        headers.ignoreCase = true
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher(['a': 'b'])

        expect:
        headersMatcher.test(requestProperties)
    }

    def "case insensitive match only applies to values, not header names"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': 'b']
        headers.ignoreUnknown = false
        headers.ignoreCase = true
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher(['A': 'b'])

        expect:
        !headersMatcher.test(requestProperties)
    }

    def "matches regex header value"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': 'b.*']
        requestProperties.headers = headers

        headersMatcher = new HeadersMatcher(['a': 'bcd'])

        expect:
        headersMatcher.test(requestProperties)
    }
}
