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

import name.abhijitsarkar.feign.model.FeignMapping
import name.abhijitsarkar.feign.model.RecordingProperties
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