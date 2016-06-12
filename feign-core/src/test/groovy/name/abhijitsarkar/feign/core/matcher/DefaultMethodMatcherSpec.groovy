package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.Method
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class DefaultMethodMatcherSpec extends Specification {
    def methodMatcher = new DefaultMethodMatcher()
    def feignMapping
    def requestProperties

    def setup() {
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties
    }

    def "matches exact method"() {
        setup:
        def method = new Method()
        method.name = 'GET'
        requestProperties.method = method
        def request = Request.builder().method('GET').build()

        expect:
        methodMatcher.apply(request, feignMapping)
    }

    def "matches regex method"() {
        setup:
        def method = new Method()
        method.name = '.*'
        requestProperties.method = method
        def request = Request.builder().method('GET').build()

        expect:
        methodMatcher.apply(request, feignMapping)
    }

    def "matches ignore case"() {
        setup:
        def method = new Method()
        method.name = 'get'
        method.ignoreCase = true
        requestProperties.method = method
        def request = Request.builder().method('GET').build()

        expect:
        methodMatcher.apply(request, feignMapping)
    }

    def "does not match method"() {
        setup:
        def method = new Method()
        method.name = 'POST'
        requestProperties.method = method
        def request = Request.builder().method('GET').build()

        expect:
        !methodMatcher.apply(request, feignMapping)
    }
}
