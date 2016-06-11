package name.abhijitsarkar.feign.persistence.service

import name.abhijitsarkar.feign.core.domain.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import name.abhijitsarkar.feign.persistence.repository.RequestRepository
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class MongoDbRecordingServiceSpec extends Specification {
    def "records when asked to"() {
        setup:
        def recordingService = new MongoDbRecordingService()
        def requestRepository = Mock(RequestRepository)
        recordingService.requestRepository = requestRepository

        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        when:
        recordingService.record(request, Optional.of(new FeignMapping()))

        then:
        1 * requestRepository.save({
            it.id != null
            it.path == request.path
            it.method == request.method
        })
    }

    def "does not record when record is false"() {
        def recordingService = new MongoDbRecordingService()
        def requestRepository = Mock(RequestRepository)
        recordingService.requestRepository = requestRepository

        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        def mapping = new FeignMapping()
        mapping.request.record = false

        when:
        recordingService.record(request, Optional.of(mapping))

        then:
        0 * requestRepository.save(_)
    }

    def "does not record when no mapping found"() {
        def recordingService = new MongoDbRecordingService()
        def requestRepository = Mock(RequestRepository)
        recordingService.requestRepository = requestRepository

        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        when:
        recordingService.record(request, Optional.empty())

        then:
        0 * requestRepository.save(_)
    }
}