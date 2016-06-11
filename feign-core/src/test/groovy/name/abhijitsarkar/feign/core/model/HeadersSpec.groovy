package name.abhijitsarkar.feign.core.model

import name.abhijitsarkar.feign.core.matcher.HeadersMatcher
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class HeadersSpec extends Specification {
    def headers

    @Unroll
    def "sets ignoreUnknown when set to #ignoreUnknown"() {
        setup:
        headers = new Headers()
        headers.ignoreUnknown = ignoreUnknown

        expect:
        headers.ignoreUnknown == (ignoreUnknown == null ? Boolean.TRUE : ignoreUnknown)

        where:
        ignoreUnknown << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets ignoreEmpty when set to #ignoreEmpty"() {
        setup:
        headers = new Headers()
        headers.ignoreEmpty = ignoreEmpty

        expect:
        headers.ignoreEmpty == (ignoreEmpty == null ? Boolean.TRUE : ignoreEmpty)

        where:
        ignoreEmpty << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets ignoreCase when set to #ignoreCase"() {
        setup:
        headers = new Headers()
        headers.ignoreCase = ignoreCase

        expect:
        headers.ignoreCase == (ignoreCase ?: Boolean.FALSE)

        where:
        ignoreCase << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets pairs when set to #pairs"() {
        setup:
        headers = new Headers()
        headers.pairs = pairs

        expect:
        headers.pairs == (pairs == null ? [:] : pairs)

        where:
        pairs << [null, [:], ['a': 'b']]
    }

    @Unroll
    def "sets matcher when set to #matcher"() {
        setup:
        headers = new Headers()
        headers.matcher = matcher

        expect:
        headers.matcher == (matcher ?: HeadersMatcher)

        where:
        matcher << [null, HeadersMatcher, TestMatcher]
    }
}