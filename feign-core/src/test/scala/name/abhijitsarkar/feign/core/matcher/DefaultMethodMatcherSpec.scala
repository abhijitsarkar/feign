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
import name.abhijitsarkar.feign.core.model._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class DefaultMethodMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val methodMatcher = new DefaultMethodMatcher
  var feignProperties: FeignProperties = _
  var method: Method = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    method = new Method
    method.setIgnoreCase(feignProperties.getIgnoreCase)

    requestProperties.method = method
  }

  "method matcher" should "match exact method" in {
    method.name = "GET"
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe true
  }

  import Method.WILDCARD

  it should "match regex method" in {
    method.name = WILDCARD
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe true
  }

  it should "match using ignore case" in {
    method.name = "get"
    method.setIgnoreCase(true)
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe true
  }

  it should "not match" in {
    method.name = "POST"
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe false
  }
}
