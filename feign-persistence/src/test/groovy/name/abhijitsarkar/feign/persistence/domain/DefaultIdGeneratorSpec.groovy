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

package name.abhijitsarkar.feign.persistence.domain

import name.abhijitsarkar.feign.Request
import spock.lang.Specification


/**
 * @author Abhijit Sarkar
 */
class DefaultIdGeneratorSpec extends Specification {
    def idGenerator = new DefaultIdGenerator()

    def "uses value from request id header"() {
        setup:
        Request request = Request.builder()
                .headers(['x-request-id': 'id'])
                .build()

        expect:
        idGenerator.id(request) == 'id'
    }

    def "generates random id if request id header is missing"() {
        setup:
        Request request = Request.builder().build()

        expect:
        idGenerator.id(request)
    }
}