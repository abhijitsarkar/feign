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

package name.abhijitsarkar.feign.core.service

import name.abhijitsarkar.feign.core.model.FeignProperties
import name.abhijitsarkar.feign.model.Queries
import name.abhijitsarkar.feign.model.RequestProperties
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
    def requestProperties

    def queries

    def setupSpec() {
        feignProperties = new FeignProperties()
        feignProperties.ignoreEmpty = null
        feignProperties.ignoreUnknown = null
        feignProperties.ignoreCase = null

        propertiesMerger = new IgnorablePropertiesMerger()
    }

    def setup() {
        requestProperties = new RequestProperties()
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