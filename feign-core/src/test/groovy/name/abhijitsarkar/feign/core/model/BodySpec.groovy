package name.abhijitsarkar.feign.core.model

import name.abhijitsarkar.feign.core.matcher.BodyMatcher
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class BodySpec extends Specification {
    def body

    @Unroll
    def "sets ignoreUnknown when set to #ignoreUnknown"() {
        setup:
        body = new Body()
        body.ignoreUnknown = ignoreUnknown

        expect:
        body.ignoreUnknown == (ignoreUnknown == null ? Boolean.TRUE : ignoreUnknown)

        where:
        ignoreUnknown << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets ignoreEmpty when set to #ignoreEmpty"() {
        setup:
        body = new Body()
        body.ignoreEmpty = ignoreEmpty

        expect:
        body.ignoreEmpty == (ignoreEmpty == null ? Boolean.TRUE : ignoreEmpty)

        where:
        ignoreEmpty << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets ignoreCase when set to #ignoreCase"() {
        setup:
        body = new Body()
        body.ignoreCase = ignoreCase

        expect:
        body.ignoreCase == (ignoreCase ?: Boolean.FALSE)

        where:
        ignoreCase << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets matcher when set to #matcher"() {
        setup:
        body = new Body()
        body.matcher = matcher

        expect:
        body.matcher == (matcher ?: BodyMatcher)

        where:
        matcher << [null, BodyMatcher, TestMatcher]
    }

    def "gets content when raw is set"() {
        setup:
        body = new Body()
        body.raw = 'body'

        expect:
        body.content == 'body'
    }

    def "gets content when url is set"() {
        setup:
        body = new Body()
        body.url = getClass().getResource('/body.txt')

        expect:
        body.content == 'body'
    }
}