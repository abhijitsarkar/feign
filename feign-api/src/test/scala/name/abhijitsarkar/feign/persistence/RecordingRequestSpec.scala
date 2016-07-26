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

package name.abhijitsarkar.feign.persistence

import java.util.Optional

import name.abhijitsarkar.feign.Request
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.collection.immutable.{HashMap => ImmutableHashMap}

/**
  * @author Abhijit Sarkar
  */
class RecordingRequestSpec extends FlatSpec with Matchers {
  "recording request" should "copy properties from given request" in {
    val request = new Request()
    request.path = "/abc"
    request.method = "GET"
    request.queryParams = ImmutableHashMap("a" -> List("b"))
    request.headers = ImmutableHashMap("a" -> "b")
    request.body = Some("body")

    val recordingRequest = new RecordingRequest().copyFrom(request)
    recordingRequest.id = "id"

    recordingRequest.id shouldEqual "id"
    recordingRequest.path shouldEqual request.path
    recordingRequest.method shouldEqual request.method
    recordingRequest.queryParams shouldEqual request.queryParams.mapValues(_.asJava).asJava
    recordingRequest.headers shouldEqual request.headers.asJava
    recordingRequest.body shouldEqual request.body.getOrElse(null)
  }
}
