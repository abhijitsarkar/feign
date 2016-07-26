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

package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.core.model.{Body, FeignMapping, ResponseProperties}
import name.abhijitsarkar.feign.core.service.FeignService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.springframework.http.HttpStatus

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class FeignControllerSpec extends FlatSpec with Matchers with BeforeAndAfter {
  var feignController: FeignController = _
  var feignService: FeignService = _
  var feignMapping: FeignMapping = _
  var responseProperties: ResponseProperties = _

  before {
    feignService = mock(classOf[FeignService])
    feignController = new FeignController(feignService)

    feignMapping = new FeignMapping
    responseProperties = new ResponseProperties
    feignMapping.response = responseProperties
  }

  "feign controller" should "return 404 response if no mapping found" in {
    when(feignService.findFeignMapping(any())).thenReturn(None, Nil: _*)

    val retVal = feignController.all(null)

    retVal.getStatusCode shouldEqual (HttpStatus.NOT_FOUND)
  }

  it should "return response with status specified by mapping" in {
    responseProperties.status = HttpStatus.OK.value()
    when(feignService.findFeignMapping(any())).thenReturn(Some(feignMapping), Nil: _*)

    val retVal = feignController.all(null)

    retVal.getStatusCode.value() shouldEqual (responseProperties.status)
  }

  it should "return headers if specified by mapping" in {
    responseProperties.headers = Map("a" -> "b").asJava
    when(feignService.findFeignMapping(any())).thenReturn(Some(feignMapping), Nil: _*)

    val retVal = feignController.all(null)

    retVal.getHeaders.toSingleValueMap should contain value ("b")
  }

  it should "return body if specified by mapping" in {
    val body = new Body
    body.setRaw("body")
    responseProperties.body = body
    when(feignService.findFeignMapping(any())).thenReturn(Some(feignMapping), Nil: _*)

    val retVal = feignController.all(null)

    retVal.getBody shouldEqual ("body")
  }
}
