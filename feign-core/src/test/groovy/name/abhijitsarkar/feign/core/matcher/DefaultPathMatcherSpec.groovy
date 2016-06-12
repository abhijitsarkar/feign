package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.Path
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class DefaultPathMatcherSpec extends Specification {
    def pathMatcher = new DefaultPathMatcher()
    def feignMapping
    def requestProperties

    def setup() {
        feignMapping = new FeignMapping()
        requestProperties = new RequestProperties()
        feignMapping.request = requestProperties
    }

    def "matches exact path"() {
        setup:
        def path = new Path()
        path.uri = '/abc/xyz'
        requestProperties.path = path
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "matches regex path"() {
        setup:
        def path = new Path()
        path.uri = '/abc/**'
        requestProperties.path = path
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "matches ignore case"() {
        setup:
        def path = new Path()
        path.uri = '/abc/xyz'
        path.ignoreCase = true
        requestProperties.path = path
        def request = Request.builder().path('/abc/xyz').build()

        expect:
        pathMatcher.apply(request, feignMapping)
    }

    def "does not match path"() {
        setup:
        def path = new Path()
        path.uri = '/abc'
        requestProperties.path = path
        def request = Request.builder().path('/xyz').build()

        expect:
        !pathMatcher.apply(request, feignMapping)
    }
}
