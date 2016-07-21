package name.abhijitsarkar.feign.persistence.service

import name.abhijitsarkar.feign.persistence.repository.MongoDbRequestRepository
import name.abhijitsarkar.feign.persistence.{RecordingRequest, RecordingService}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.PayloadApplicationEvent

/**
  * @author Abhijit Sarkar
  */
class MongoDbRecordingService @Autowired()(private val mongoDbRequestRepository: MongoDbRequestRepository)
  extends RecordingService {
  val logger = LoggerFactory.getLogger(classOf[MongoDbRecordingService])

  def record(requestEvent: PayloadApplicationEvent[RecordingRequest]): Unit = {
    val recordingRequest: RecordingRequest = requestEvent.getPayload
    val id: String = recordingRequest.getId

    logger.info("Recording request with id: {}.", id)

    mongoDbRequestRepository.save(recordingRequest)
  }
}
