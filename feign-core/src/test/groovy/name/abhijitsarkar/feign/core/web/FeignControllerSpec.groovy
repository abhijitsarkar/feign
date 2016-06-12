package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.core.model.Body
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.core.model.ResponseProperties
import name.abhijitsarkar.feign.core.service.FeignService
import org.springframework.http.HttpStatus
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class FeignControllerSpec extends Specification {
    FeignController feignController
    def feignService
    def feignMapping
    def responseProperties

    def setup() {
        feignController = new FeignController()
        feignService = Mock(FeignService)
        feignController.feignService = feignService

        feignMapping = new FeignMapping()
        responseProperties = new ResponseProperties()
        feignMapping.response = responseProperties
    }

    def "returns 404 response if no mapping found"() {
        setup:
        feignService.findFeignMapping(_) >> Optional.empty()

        when:
        def retVal = feignController.all(null)

        then:
        retVal.getStatusCode() == HttpStatus.NOT_FOUND
    }

    def "returns response with status specified by mapping"() {
        setup:
        responseProperties.status = HttpStatus.OK.value()

        feignService.findFeignMapping(_) >> Optional.of(feignMapping)

        when:
        def retVal = feignController.all(null)

        then:
        retVal.getStatusCode().value() == responseProperties.status
    }

    def "returns headers if specified by mapping"() {
        setup:
        responseProperties.headers = ['a': 'b']

        feignService.findFeignMapping(_) >> Optional.of(feignMapping)

        when:
        def retVal = feignController.all(null)

        then:
        retVal.headers.toSingleValueMap() == responseProperties.headers
    }

    def "returns body if specified by mapping"() {
        setup:
        def body = new Body()
        body.raw = 'body'
        responseProperties.body = body

        feignService.findFeignMapping(_) >> Optional.of(feignMapping)

        when:
        def retVal = feignController.all(null)

        then:
        retVal.body == 'body'
    }
}