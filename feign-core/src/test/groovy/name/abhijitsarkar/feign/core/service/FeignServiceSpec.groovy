package name.abhijitsarkar.feign.core.service

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.matcher.DefaultBodyMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultHeadersMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultMethodMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultPathMatcher
import name.abhijitsarkar.feign.core.matcher.DefaultQueriesMatcher
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.FeignProperties
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class FeignServiceSpec extends Specification {
    def matchers = [new DefaultPathMatcher(), new DefaultMethodMatcher(), new DefaultQueriesMatcher()
                    , new DefaultHeadersMatcher(), new DefaultBodyMatcher()]
    def feignService
    def eventPublisher = Mock(ApplicationEventPublisher)

    def setup() {
        feignService = new FeignService()
        def feignProperties = new FeignProperties()
        feignProperties.mappings = [new FeignMapping()] as List

        feignService.feignProperties = feignProperties
        feignService.matchers = matchers
        feignService.eventPublisher = eventPublisher
    }

    def "findsFeignMapping and publishes request event"() {
        setup:
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        mapping.present
        1 * eventPublisher.publishEvent(request)
    }
}