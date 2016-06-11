package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.core.model.Method
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class MethodMatcherSpec extends Specification {
    def methodMatcher
    def requestProperties

    def setup() {
        requestProperties = new RequestProperties()
    }

    def "matches exact method"() {
        setup:
        def method = new Method()
        method.name = 'GET'
        requestProperties.method = method

        methodMatcher = new MethodMatcher('GET')

        expect:
        methodMatcher.test(requestProperties)
    }

    def "matches regex method"() {
        setup:
        def method = new Method()
        method.name = '.*'
        requestProperties.method = method

        methodMatcher = new MethodMatcher('GET')

        expect:
        methodMatcher.test(requestProperties)
    }

    def "matches ignore case"() {
        setup:
        def method = new Method()
        method.ignoreCase = true
        method.name = 'get'
        requestProperties.method = method

        methodMatcher = new MethodMatcher('GET')

        expect:
        methodMatcher.test(requestProperties)
    }

    def "does not match method"() {
        setup:
        def method = new Method()
        method.ignoreCase = true
        method.name = 'get'
        requestProperties.method = method

        methodMatcher = new MethodMatcher('POST')

        expect:
        !methodMatcher.test(requestProperties)
    }

    def "throws exception if initialized with null method"() {
        when:
        new MethodMatcher(null)

        then:
        thrown(NullPointerException)
    }
}
