package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.core.model.Path
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class PathMatcherSpec extends Specification {
    def pathMatcher
    def requestProperties

    def setup() {
        requestProperties = new RequestProperties()
    }

    def "matches exact path"() {
        setup:
        def path = new Path()
        path.uri = '/abc/xyz'
        requestProperties.path = path

        pathMatcher = new PathMatcher(path.uri)

        expect:
        pathMatcher.test(requestProperties)
    }

    def "matches regex path"() {
        setup:
        def path = new Path()
        path.uri = '/abc/**'
        requestProperties.path = path

        pathMatcher = new PathMatcher('/abc/xyz')

        expect:
        pathMatcher.test(requestProperties)
    }

    def "matches ignore case"() {
        setup:
        def path = new Path()
        path.uri = '/abc/**'
        path.ignoreCase = true
        requestProperties.path = path

        pathMatcher = new PathMatcher('/ABC/xyz')

        expect:
        pathMatcher.test(requestProperties)
    }

    def "does not match path"() {
        setup:
        def path = new Path()
        path.uri = '/abc/**'
        requestProperties.path = path

        pathMatcher = new PathMatcher('/xyz/abc')

        expect:
        !pathMatcher.test(requestProperties)
    }

    def "throws exception if initialized with null path"() {
        when:
        new PathMatcher(null)

        then:
        thrown(NullPointerException)
    }
}
