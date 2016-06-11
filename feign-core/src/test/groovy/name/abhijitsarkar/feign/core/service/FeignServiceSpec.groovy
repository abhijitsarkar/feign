package name.abhijitsarkar.feign.core.service

import name.abhijitsarkar.feign.core.domain.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.FeignProperties
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class FeignServiceSpec extends Specification {
    def "findsFeignMapping"() {
        setup:
        def feignService = new FeignService()
        feignService.recordingService = Mock(RecordingService)
        def feignProperties = new FeignProperties()
        feignProperties.mappings = [new FeignMapping()] as List

        feignService.feignProperties = feignProperties
        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        when:
        def mapping = feignService.findFeignMapping(request)

        then:
        mapping.present
    }
}