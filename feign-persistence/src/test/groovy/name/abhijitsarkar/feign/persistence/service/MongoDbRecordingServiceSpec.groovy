package name.abhijitsarkar.feign.persistence.service

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.persistence.domain.DefaultIdGenerator
import name.abhijitsarkar.feign.persistence.repository.RequestRepository
import org.springframework.context.PayloadApplicationEvent
import spock.lang.Specification
/**
 * @author Abhijit Sarkar
 */
class MongoDbRecordingServiceSpec extends Specification {
    def "records"() {
        setup:
        def recordingService = new MongoDbRecordingService()
        def requestRepository = Mock(RequestRepository)
        recordingService.requestRepository = requestRepository
        recordingService.idGenerator = new DefaultIdGenerator()

        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        def event = new PayloadApplicationEvent<Request>(this, request)

        when:
        recordingService.record(event)

        then:
        1 * requestRepository.save({
            it.id != null
            it.path == request.path
            it.method == request.method
        })
    }
}