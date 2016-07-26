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

package name.abhijitsarkar.feign.core.model

import java.util.{Collections, Map => JavaMap}

import org.springframework.http.HttpStatus.OK

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class ResponseProperties {
  var status: Integer = _
  var headers: JavaMap[String, String] = _
  var body: Body = _

  setStatus(null)
  setHeaders(null)
  setBody(null)

  def getStatus = status

  def setStatus(status: Integer) {
    this.status = if (status == null) Integer.valueOf(OK.value)
    else status
  }

  def headersToScala = headers.asScala.toMap

  def getHeaders = headers

  def setHeaders(headers: JavaMap[String, String]) {
    this.headers = if (headers == null) Collections.emptyMap()
    else headers
  }

  def getBody = body

  def setBody(body: Body) {
    this.body = if (body == null) new Body
    else body
  }

  override def toString = s"ResponseProperties($status, $headers, $body)"
}
