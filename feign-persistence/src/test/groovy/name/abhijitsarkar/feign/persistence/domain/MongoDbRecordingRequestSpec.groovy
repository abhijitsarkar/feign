package name.abhijitsarkar.feign.persistence.domain

import name.abhijitsarkar.feign.Request
import spock.lang.Specification


/**
 * @author Abhijit Sarkar
 */
class MongoDbRecordingRequestSpec extends Specification {
    def "copies properties from given request"() {
        setup:
        Request request = Request.builder()
                .path('/abc')
                .method('GET')
                .queryParams(['a': ['b'] as String[]])
                .headers(['a': 'b'])
                .body('body')
                .build()

        when:
        def recordingRequest = new MongoDbRecordingRequest(request, 'id')

        then:
        recordingRequest.id == 'id'
        recordingRequest.path == request.path
        recordingRequest.method == request.method
        recordingRequest.queryParams == request.queryParams
        recordingRequest.headers == request.headers
        recordingRequest.body == request.body
    }
}