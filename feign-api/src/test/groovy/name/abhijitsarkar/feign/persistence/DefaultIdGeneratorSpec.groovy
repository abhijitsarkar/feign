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
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class DefaultIdGeneratorSpec extends Specification {
    def idGenerator = new DefaultIdGenerator()

    def "generates id for absolute path"() {
        given:
        Request request = Request.builder()
                .path('/abc/xyz')
                .build();

        expect:
        idGenerator.id(request).startsWith('abc-')
    }

    def "generates id for relative path"() {
        given:
        Request request = Request.builder()
                .path('abc/xyz')
                .build();

        expect:
        idGenerator.id(request).startsWith('abc-')
    }
}