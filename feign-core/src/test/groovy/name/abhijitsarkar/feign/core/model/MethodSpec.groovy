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
class MethodSpec extends Specification {
    def method

    @Unroll
    def "sets name when set to #name"() {
        setup:
        method = new Method()
        method.name = name

        expect:
        method.name == (name ?: Method.WILDCARD)

        where:
        name << [null, Method.WILDCARD, 'name']
    }

    @Unroll
    def "sets ignoreCase when set to #ignoreCase"() {
        setup:
        method = new Method()
        method.ignoreCase = ignoreCase

        expect:
        method.ignoreCase == (ignoreCase ?: Boolean.FALSE)

        where:
        ignoreCase << [null, Boolean.TRUE, Boolean.FALSE]
    }
}