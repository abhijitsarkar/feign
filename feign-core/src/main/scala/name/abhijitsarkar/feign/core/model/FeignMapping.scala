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

/**
  * @author Abhijit Sarkar
  */
class FeignMapping {
  var request: RequestProperties = _
  var response: ResponseProperties = _

  setRequest(null)
  setResponse(null)

  def getRequest = request

  def setRequest(requestProperties: RequestProperties) {
    this.request = if (requestProperties == null) new RequestProperties
    else requestProperties
  }

  def getResponse = response

  def setResponse(responseProperties: ResponseProperties) {
    this.response = if (responseProperties == null) new ResponseProperties
    else responseProperties
  }

  override def toString = s"FeignMapping($request, $response)"
}
