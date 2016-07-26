/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 */

package name.abhijitsarkar.feign.core.service

import name.abhijitsarkar.feign.core.model.{FeignProperties, Queries, RequestProperties}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class IgnorablePropertiesMergerSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val feignProperties = new FeignProperties
  val propertiesMerger = new IgnorablePropertiesMerger
  var requestProperties: RequestProperties = _
  var queries: Queries = _

  before {
    feignProperties.postConstruct
    requestProperties = new RequestProperties()
    queries = new Queries
  }

  "properties merger" should "copy from global if local is null" in {
    requestProperties.queries = queries

    propertiesMerger.merge(requestProperties, feignProperties)

    queries.getIgnoreEmpty shouldEqual feignProperties.getIgnoreEmpty
    queries.getIgnoreUnknown shouldEqual feignProperties.getIgnoreUnknown
    queries.getIgnoreCase shouldEqual feignProperties.getIgnoreCase
  }

  it should "keep local properties if defined" in {
    queries.setIgnoreEmpty(!feignProperties.getIgnoreEmpty)
    queries.setIgnoreUnknown(!feignProperties.getIgnoreUnknown)
    queries.setIgnoreCase(!feignProperties.getIgnoreCase)
    requestProperties.queries = queries

    propertiesMerger.merge(requestProperties, feignProperties)

    queries.getIgnoreEmpty shouldNot equal(feignProperties.getIgnoreEmpty)
    queries.getIgnoreUnknown shouldNot equal(feignProperties.getIgnoreUnknown)
    queries.getIgnoreCase shouldNot equal(feignProperties.getIgnoreCase)
  }
}
