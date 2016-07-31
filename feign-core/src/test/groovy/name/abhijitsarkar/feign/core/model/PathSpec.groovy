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