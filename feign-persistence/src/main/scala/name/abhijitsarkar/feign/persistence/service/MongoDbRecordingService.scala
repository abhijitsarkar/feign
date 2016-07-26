/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 */

package name.abhijitsarkar.feign.persistence.service

import name.abhijitsarkar.feign.persistence.repository.MongoDbRequestRepository
import name.abhijitsarkar.feign.persistence.{RecordingRequest, RecordingService}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.PayloadApplicationEvent
import org.springframework.context.event.EventListener

/**
  * @author Abhijit Sarkar
  */
class MongoDbRecordingService @Autowired()(private val mongoDbRequestRepository: MongoDbRequestRepository)
  extends RecordingService {
  val logger = LoggerFactory.getLogger(classOf[MongoDbRecordingService])

  @EventListener
  def record(requestEvent: PayloadApplicationEvent[RecordingRequest]) {
    val recordingRequest: RecordingRequest = requestEvent.getPayload
    val id: String = recordingRequest.getId

    logger.info("Recording request with id: {}.", id)

    mongoDbRequestRepository.save(recordingRequest)
  }
}
