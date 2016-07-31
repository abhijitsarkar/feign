/*
 * Copyright (c) 2016, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * License for more details.
 */

package name.abhijitsarkar.feign.persistence.service

import name.abhijitsarkar.feign.persistence.RecordingRequest
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