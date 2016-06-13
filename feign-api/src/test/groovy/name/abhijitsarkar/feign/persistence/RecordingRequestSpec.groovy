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