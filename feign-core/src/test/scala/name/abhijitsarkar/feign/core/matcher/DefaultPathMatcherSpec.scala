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

package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.{FeignMapping, FeignProperties, Path, RequestProperties}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class DefaultPathMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val pathMatcher = new DefaultPathMatcher
  var feignProperties: FeignProperties = _
  var path: Path = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    path = new Path
    path.setIgnoreCase(feignProperties.getIgnoreCase)

    requestProperties.path = path
  }

  "path matcher" should "match exact path" in {
    path.uri = "/abc/xyz"
    val request = new Request
    request.path = "/abc/xyz"

    pathMatcher(request, feignMapping) shouldBe true
  }

  it should "match regex path" in {
    path.uri = "/abc/*"
    val request = new Request
    request.path = "/abc/xyz"

    pathMatcher(request, feignMapping) shouldBe true
  }

  it should "match using ignore case" in {
    path.uri = "/abc/xyz"
    path.setIgnoreCase(true)
    val request = new Request
    request.path = "/abc/XYZ"

    pathMatcher(request, feignMapping) shouldBe true
  }

  it should "not match" in {
    path.uri = "/abc"
    val request = new Request
    request.path = "/xyz"

    pathMatcher(request, feignMapping) shouldBe false
  }
}
