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