package name.abhijitsarkar.feign.core.model

import name.abhijitsarkar.feign.core.matcher.QueriesMatcher
import spock.lang.Specification
import spock.lang.Unroll
/**
 * @author Abhijit Sarkar
 */
class QueriesSpec extends Specification {
    def queries

    @Unroll
    def "sets ignoreUnknown when set to #ignoreUnknown"() {
        setup:
        queries = new Queries()
        queries.ignoreUnknown = ignoreUnknown

        expect:
        queries.ignoreUnknown == (ignoreUnknown == null ? Boolean.TRUE : ignoreUnknown)

        where:
        ignoreUnknown << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets ignoreEmpty when set to #ignoreEmpty"() {
        setup:
        queries = new Queries()
        queries.ignoreEmpty = ignoreEmpty

        expect:
        queries.ignoreEmpty == (ignoreEmpty == null ? Boolean.TRUE : ignoreEmpty)

        where:
        ignoreEmpty << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets ignoreCase when set to #ignoreCase"() {
        setup:
        queries = new Queries()
        queries.ignoreCase = ignoreCase

        expect:
        queries.ignoreCase == (ignoreCase ?: Boolean.FALSE)

        where:
        ignoreCase << [null, Boolean.TRUE, Boolean.FALSE]
    }

    @Unroll
    def "sets pairs when set to #pairs"() {
        setup:
        queries = new Queries()
        queries.pairs = pairs

        expect:
        queries.pairs == (pairs == null ? [:] : pairs)

        where:
        pairs << [null, [:], ['a': ['b'] as List]]
    }

    @Unroll
    def "sets matcher when set to #matcher"() {
        setup:
        queries = new Queries()
        queries.matcher = matcher

        expect:
        queries.matcher == (matcher ?: QueriesMatcher)

        where:
        matcher << [null, QueriesMatcher, TestMatcher]
    }
}