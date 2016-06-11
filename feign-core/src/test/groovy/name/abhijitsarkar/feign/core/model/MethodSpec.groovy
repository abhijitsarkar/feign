package name.abhijitsarkar.feign.core.model

import name.abhijitsarkar.feign.core.matcher.MethodMatcher
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

    @Unroll
    def "sets matcher when set to #matcher"() {
        setup:
        method = new Method()
        method.matcher = matcher

        expect:
        method.matcher == (matcher ?: MethodMatcher)

        where:
        matcher << [null, MethodMatcher, TestMatcher]
    }
}