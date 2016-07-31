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