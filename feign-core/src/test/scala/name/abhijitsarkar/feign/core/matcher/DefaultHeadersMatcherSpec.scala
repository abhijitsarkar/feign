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

import java.util.{List => JavaList, Map => JavaMap}

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class DefaultHeadersMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val headersMatcher = new DefaultHeadersMatcher
  var feignProperties: FeignProperties = _
  var headers: Headers = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    headers = new Headers
    headers.setIgnoreCase(feignProperties.getIgnoreCase)
    headers.setIgnoreUnknown(feignProperties.getIgnoreUnknown)
    headers.setIgnoreEmpty(feignProperties.getIgnoreEmpty)

    requestProperties.headers = headers
  }

  "headers matcher" should "match when no request headers and no properties headers" in {
    val request = new Request

    headersMatcher(request, feignMapping) shouldBe true
  }

  it should "honor ignore unknown" in {
    val ignoreUnknown = Table("ignoreUnknown", true, false)
    val request = new Request
    request.setHeaders(Map("a" -> "b"))

    forAll(ignoreUnknown) { x =>
      headers.setIgnoreUnknown(x)

      headersMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore empty" in {
    val ignoreEmpty = Table("ignoreEmpty", true, false)
    val request = new Request
    headers.setPairs(Map("a" -> "b").asJava)

    forAll(ignoreEmpty) { x =>
      headers.setIgnoreEmpty(x)

      headersMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore case" in {
    val ignoreCase = Table("ignoreCase", true, false)
    val request = new Request
    request.setHeaders(Map("a" -> "B"))
    headers.setPairs(Map("a" -> "b").asJava)
    headers.setIgnoreUnknown(false)

    forAll(ignoreCase) { x =>
      headers.setIgnoreCase(x)

      headersMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "match regex param values" in {
    val request = new Request
    request.setHeaders(Map("a" -> "bc"))
    headers.setPairs(Map("a" -> "b.*").asJava)

    headersMatcher(request, feignMapping) shouldBe true
  }
}
