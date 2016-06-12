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
class IgnorableRequestPropertiesSpec extends Specification {
    private static final class TestIgnorableRequestProperties extends IgnorableRequestProperties {
    }

    @Unroll
    def "sets #ignoreProperty"() {
        setup:
        def testUnit = new TestIgnorableRequestProperties()
        def values = [null, Boolean.TRUE, Boolean.FALSE]

        expect:
        !values.collect {
            testUnit."$ignoreProperty" = it
            testUnit."$ignoreProperty" == it
        }.contains(false)

        where:
        ignoreProperty   | _
        'ignoreUnknown'  | _
        'ignoreCase'     | _
        'ignoreEmpty'    | _
    }
}
