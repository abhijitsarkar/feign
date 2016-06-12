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
import spock.lang.Unroll

/**
 * @author Abhijit Sarkar
 */
class PathSpec extends Specification {
    def path

    @Unroll
    def "sets uri when set to #uri"() {
        setup:
        path = new Path()
        path.uri = uri

        expect:
        path.uri == (uri ?: Path.WILDCARD_PATTERN)

        where:
        uri << [null, Path.WILDCARD_PATTERN, '/a']
    }

    @Unroll
    def "sets ignoreCase when set to #ignoreCase"() {
        setup:
        path = new Path()
        path.ignoreCase = ignoreCase

        expect:
        path.ignoreCase == (ignoreCase ?: Boolean.FALSE)

        where:
        ignoreCase << [null, Boolean.TRUE, Boolean.FALSE]
    }
}