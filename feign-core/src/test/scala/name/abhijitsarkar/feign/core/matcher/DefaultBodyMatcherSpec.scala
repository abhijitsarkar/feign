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

/**
  * @author Abhijit Sarkar
  */
class DefaultBodyMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val bodyMatcher = new DefaultBodyMatcher
  var feignProperties: FeignProperties = _
  var body: Body = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    body = new Body
    body.setIgnoreCase(feignProperties.getIgnoreCase)
    body.setIgnoreUnknown(feignProperties.getIgnoreUnknown)
    body.setIgnoreEmpty(feignProperties.getIgnoreEmpty)

    requestProperties.body = body
  }

  "body matcher" should "match when no request body and no properties body" in {
    val request = new Request

    bodyMatcher(request, feignMapping) shouldBe true
  }

  it should "honor ignore unknown" in {
    val ignoreUnknown = Table("ignoreUnknown", true, false)
    val request = new Request
    request.setBody(Some("body"))
    body.raw = ""

    forAll(ignoreUnknown) { x =>
      body.setIgnoreUnknown(x)

      bodyMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore empty" in {
    val ignoreEmpty = Table("ignoreEmpty", true, false)
    val request = new Request
    request.setBody(None)
    body.raw = ""

    forAll(ignoreEmpty) { x =>
      body.setIgnoreEmpty(x)

      bodyMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore case" in {
    val ignoreCase = Table("ignoreCase", true, false)
    val request = new Request
    request.setBody(Some("BODY"))
    body.raw = "body"

    forAll(ignoreCase) { x =>
      body.setIgnoreCase(x)

      bodyMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "match exact content" in {
    val request = new Request
    request.setBody(Some("body"))
    body.raw = "body"

    bodyMatcher(request, feignMapping) shouldBe true
  }

  it should "match regex content" in {
    val request = new Request
    request.setBody(Some("body"))
    body.raw = "b.*"

    bodyMatcher(request, feignMapping) shouldBe true
  }
}
