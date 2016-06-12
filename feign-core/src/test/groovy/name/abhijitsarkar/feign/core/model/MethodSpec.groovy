package name.abhijitsarkar.feign.core.model

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class MethodSpec extends Specification {
    def method

    @Unroll
    def "sets name when set to #name"() {
        setup:
        method = new Method()
        method.name = name

        expect:
        method.name == (name ?: Method.WILDCARD)

        where:
        name << [null, Method.WILDCARD, 'name']
    }

    @Unroll
    def "sets ignoreCase when set to #ignoreCase"() {
        setup:
        method = new Method()
        method.ignoreCase = ignoreCase

        expect:
        method.ignoreCase == (ignoreCase ?: Boolean.FALSE)

        where:
        ignoreCase << [null, Boolean.TRUE, Boolean.FALSE]
    }
}