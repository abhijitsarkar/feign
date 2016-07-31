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

package name.abhijitsarkar.feign.core.model

import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class BodySpec extends Specification {
    def body

    def "gets content when raw is set"() {
        setup:
        body = new Body()
        body.raw = 'body'

        expect:
        body.content == 'body'
    }

    def "gets content when url is set"() {
        setup:
        body = new Body()
        body.url = getClass().getResource('/body.txt')

        expect:
        body.content == 'body'
    }
}