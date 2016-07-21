package name.abhijitsarkar.feign.persistence.service

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.persistence.RecordingRequest
import name.abhijitsarkar.feign.persistence.repository.MongoDbRequestRepository
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.{times, verify}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import org.springframework.context.PayloadApplicationEvent

/**
  * @author Abhijit Sarkar
  */
class MongoDbRecordingServiceSpec extends FlatSpec with Matchers with MockitoSugar {
  "MongoDbRecordingService" should "record" in {
    val request = new Request()
    request.path = "/a"
    request.method = "GET"

    val recordingRequest = new RecordingRequest().copyFrom(request)
    recordingRequest.id = "1"

    val requestRepository = mock[MongoDbRequestRepository]
    val recordingService = new MongoDbRecordingService(requestRepository)
    val event = new PayloadApplicationEvent[RecordingRequest](this, recordingRequest)

    recordingService.record(event)

    val argument = ArgumentCaptor.forClass(classOf[RecordingRequest])
    verify(requestRepository).save(argument.capture())

    val arg: RecordingRequest = argument.getValue

    arg.id shouldEqual ("1")
    arg.path shouldEqual (request.path)
    arg.method shouldEqual (request.method)
  }
}
