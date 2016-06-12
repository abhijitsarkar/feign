package name.abhijitsarkar.feign.core.model

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class HeadersSpec extends Specification {
    def headers

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
}