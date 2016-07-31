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
class QueriesSpec extends Specification {
    def queries

    @Unroll
    def "sets pairs when set to #pairs"() {
        setup:
        queries = new Queries()
        queries.pairs = pairs

        expect:
        queries.pairs == (pairs == null ? [:] : pairs)

        where:
        pairs << [null, [:], ['a': ['b'] as List]]
    }
}