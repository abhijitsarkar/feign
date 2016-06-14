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
class FeignPropertiesSpec extends Specification {
    def "initializes all fields"() {
        setup:
        def testUnit = new FeignProperties()

        when:
        testUnit.postConstruct()

        then:
        testUnit.ignoreUnknown != null
        testUnit.ignoreEmpty != null
        testUnit.ignoreCase != null
        testUnit.mappings != null
        testUnit.recording != null
    }

    @Unroll
    def "sets ignoreUnknown to #output when given #input"() {
        setup:
        def testUnit = new FeignProperties()

        when:
        testUnit.ignoreUnknown = input

        then:
        testUnit.ignoreUnknown == output

        where:
        input         | output
        null          | Boolean.TRUE
        Boolean.TRUE  | Boolean.TRUE
        Boolean.FALSE | Boolean.FALSE
    }

    @Unroll
    def "sets ignoreEmpty to #output when given #input"() {
        setup:
        def testUnit = new FeignProperties()

        when:
        testUnit.ignoreEmpty = input

        then:
        testUnit.ignoreEmpty == output

        where:
        input         | output
        null          | Boolean.TRUE
        Boolean.TRUE  | Boolean.TRUE
        Boolean.FALSE | Boolean.FALSE
    }

    @Unroll
    def "sets ignoreCase to #output when given #input"() {
        setup:
        def testUnit = new FeignProperties()

        when:
        testUnit.ignoreCase = input

        then:
        testUnit.ignoreCase == output

        where:
        input         | output
        null          | Boolean.FALSE
        Boolean.TRUE  | Boolean.TRUE
        Boolean.FALSE | Boolean.FALSE
    }

    def "sets mappings"() {
        setup:
        def testUnit = new FeignProperties()

        when:
        testUnit.mappings = mappings

        then:
        testUnit.mappings != null

        where:
        mappings << [null, [new FeignMapping()]]
    }

    def "sets recording"() {
        setup:
        def testUnit = new FeignProperties()

        when:
        testUnit.recording = recording

        then:
        testUnit.recording

        where:
        recording << [null, new RecordingProperties()]
    }
}