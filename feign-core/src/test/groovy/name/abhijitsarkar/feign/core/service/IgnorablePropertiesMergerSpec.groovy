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

package name.abhijitsarkar.feign.core.service

import name.abhijitsarkar.feign.core.model.FeignProperties
import name.abhijitsarkar.feign.core.model.Queries
import name.abhijitsarkar.feign.core.model.RequestProperties
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class IgnorablePropertiesMergerSpec extends Specification {
    @Shared
    def feignProperties
    @Shared
    def propertiesMerger
    @Shared
    def requestProperties

    def queries

    def setupSpec() {
        feignProperties = new FeignProperties()
        feignProperties.ignoreEmpty = null
        feignProperties.ignoreUnknown = null
        feignProperties.ignoreCase = null

        propertiesMerger = new IgnorablePropertiesMerger()
        requestProperties = new RequestProperties()
    }

    def setup() {
        queries = new Queries()
    }

    def "copies from global if local is null"() {
        given:
        requestProperties.queries = queries

        when:
        propertiesMerger.merge(requestProperties, feignProperties)

        then:
        queries.ignoreEmpty == feignProperties.ignoreEmpty
        queries.ignoreUnknown == feignProperties.ignoreUnknown
        queries.ignoreCase == feignProperties.ignoreCase
    }

    def "keeps local if defined"() {
        given:
        queries.ignoreEmpty = !feignProperties.ignoreEmpty
        queries.ignoreUnknown = !feignProperties.ignoreUnknown
        queries.ignoreCase = !feignProperties.ignoreCase

        requestProperties.queries = queries

        when:
        propertiesMerger.merge(requestProperties, feignProperties)

        then:
        queries.ignoreEmpty == !feignProperties.ignoreEmpty
        queries.ignoreUnknown == !feignProperties.ignoreUnknown
        queries.ignoreCase == !feignProperties.ignoreCase
    }
}