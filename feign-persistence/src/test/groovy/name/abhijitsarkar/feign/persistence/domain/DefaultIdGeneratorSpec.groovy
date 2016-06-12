package name.abhijitsarkar.feign.persistence.domain

import name.abhijitsarkar.feign.Request
import spock.lang.Specification


/**
 * @author Abhijit Sarkar
 */
class DefaultIdGeneratorSpec extends Specification {
    def idGenerator = new DefaultIdGenerator()

    def "uses value from request id header"() {
        setup:
        Request request = Request.builder()
                .headers(['x-request-id': 'id'])
                .build()

        expect:
        idGenerator.id(request) == 'id'
    }

    def "generates random id if request id header is missing"() {
        setup:
        Request request = Request.builder().build()

        expect:
        idGenerator.id(request)
    }
}