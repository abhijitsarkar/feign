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

package name.abhijitsarkar.feign.persistence

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.persistence.RecordingRequest
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class RecordingRequestSpec extends Specification {
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
        def recordingRequest = new RecordingRequest(request, 'id')

        then:
        recordingRequest.id == 'id'
        recordingRequest.path == request.path
        recordingRequest.method == request.method
        recordingRequest.queryParams == request.queryParams
        recordingRequest.headers == request.headers
        recordingRequest.body == request.body
    }
}