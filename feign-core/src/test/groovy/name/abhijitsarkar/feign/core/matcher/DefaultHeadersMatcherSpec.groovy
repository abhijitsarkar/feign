package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.Headers
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class DefaultHeadersMatcherSpec extends Specification {
    def headersMatcher = new DefaultHeadersMatcher()
    def feignMapping
    def requestProperties

    def setup() {
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties
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
        def headers = new Headers()
        headers.ignoreUnknown = ignoreUnknown
        requestProperties.headers = headers

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
        def headers = new Headers()
        headers.ignoreEmpty = ignoreEmpty
        headers.pairs = ['a': 'b']
        requestProperties.headers = headers

        def request = Request.builder().build()

        expect:
        headersMatcher.apply(request, feignMapping) == ignoreEmpty

        where:
        ignoreEmpty << [Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "matches when a request header and the corresponding property header both have #values values"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': value]
        requestProperties.headers = headers

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
        def headers = new Headers()
        headers.pairs = ['a': 'B']
        headers.ignoreCase = ignoreCase
        headers.ignoreUnknown = false
        requestProperties.headers = headers

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
        def headers = new Headers()
        headers.pairs = ['a': 'b']
        headers.ignoreUnknown = false
        headers.ignoreCase = true
        requestProperties.headers = headers

        def request = Request.builder()
                .headers(['A': 'b'])
                .build()

        expect:
        !headersMatcher.apply(request, feignMapping)
    }

    def "matches regex header value"() {
        setup:
        def headers = new Headers()
        headers.pairs = ['a': 'b.*']
        requestProperties.headers = headers

        def request = Request.builder()
                .headers(['A': 'bcd'])
                .build()

        expect:
        headersMatcher.apply(request, feignMapping)
    }
}
