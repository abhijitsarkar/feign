package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.core.model.Body
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class BodyMatcherSpec extends Specification {
    def bodyMatcher
    def requestProperties

    def setup() {
        requestProperties = new RequestProperties()
    }

    def "matches when no request body and empty properties body"() {
        setup:
        def body = new Body()
        body.raw = ''
        requestProperties.body = body

        bodyMatcher = new BodyMatcher(null)

        expect:
        bodyMatcher.test(requestProperties)
    }

    def "matches when no request body, non empty properties body and ignoreEmpty is true"() {
        setup:
        def body = new Body()
        body.raw = 'body'
        requestProperties.body = body

        bodyMatcher = new BodyMatcher(null)

        expect:
        bodyMatcher.test(requestProperties)
    }

    def "does not match when no request body, non empty properties body and ignoreEmpty is false"() {
        setup:
        def body = new Body()
        body.raw = 'body'
        body.ignoreEmpty = false
        requestProperties.body = body

        bodyMatcher = new BodyMatcher(null)

        expect:
        !bodyMatcher.test(requestProperties)
    }

    def "matches exact content"() {
        setup:
        def body = new Body()
        body.raw = 'body'
        requestProperties.body = body

        bodyMatcher = new BodyMatcher('body')

        expect:
        bodyMatcher.test(requestProperties)
    }

    def "matches regex content"() {
        setup:
        def body = new Body()
        body.raw = 'b.*'
        requestProperties.body = body

        bodyMatcher = new BodyMatcher('body')

        expect:
        bodyMatcher.test(requestProperties)
    }

    def "does not match mixed case content if ignoreCase is false"() {
        setup:
        def body = new Body()
        body.raw = 'body'
        body.ignoreCase = false
        requestProperties.body = body

        bodyMatcher = new BodyMatcher('BODY')

        expect:
        !bodyMatcher.test(requestProperties)
    }

    def "matches mixed case content if ignoreCase is true"() {
        setup:
        def body = new Body()
        body.raw = 'body'
        requestProperties.body = body

        bodyMatcher = new BodyMatcher('BODY')

        expect:
        !bodyMatcher.test(requestProperties)
    }

    def "does not match when unknown request body and ignoreUnknown is false"() {
        setup:
        def body = new Body()
        body.ignoreUnknown = false
        requestProperties.body = body

        bodyMatcher = new BodyMatcher('body')

        expect:
        !bodyMatcher.test(requestProperties)
    }

    def "matches when unknown request body and ignoreUnknown is true"() {
        def body = new Body()
        requestProperties.body = body

        bodyMatcher = new BodyMatcher('body')

        expect:
        bodyMatcher.test(requestProperties)
    }
}
