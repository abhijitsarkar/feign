/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *  *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 *
 */

package name.abhijitsarkar.feign.persistence.service

import name.abhijitsarkar.feign.RecordingRequest
import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.persistence.repository.MongoDbRequestRepository
import org.springframework.context.PayloadApplicationEvent
import spock.lang.Specification
/**
 * @author Abhijit Sarkar
 */
class MongoDbRecordingServiceSpec extends Specification {
    def "records"() {
        setup:
        def recordingService = new MongoDbRecordingService()
        def requestRepository = Mock(MongoDbRequestRepository)
        recordingService.mongoDbRequestRepository = requestRepository

        def request = Request.builder()
                .path('/a')
                .method('GET')
                .build()

        def event = new PayloadApplicationEvent<RecordingRequest>(this, new RecordingRequest(request, '1'))

        when:
        recordingService.record(event)

        then:
        1 * requestRepository.save({
            it.id == 1
            it.path == request.path
            it.method == request.method
        })
    }
}