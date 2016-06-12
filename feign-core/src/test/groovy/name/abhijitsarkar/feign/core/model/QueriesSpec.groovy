package name.abhijitsarkar.feign.core.model

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class QueriesSpec extends Specification {
    def queries

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
}