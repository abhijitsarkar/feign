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
class RequestProperties {
  var recording: RecordingProperties = _
  var path: Path = _
  var method: Method = _
  var queries: Queries = _
  var headers: Headers = _
  var body: Body = _

  setPath(null)
  setMethod(null)
  setQueries(null)
  setHeaders(null)
  setBody(null)
  setRecording(null)

  def getPath = path

  def setPath(path: Path) {
    this.path = if (path == null) new Path
    else path
  }

  def getMethod = method

  def setMethod(method: Method) {
    this.method = if (method == null) new Method
    else method
  }

  def getQueries = queries

  def setQueries(queries: Queries) {
    this.queries = if (queries == null) new Queries
    else queries
  }

  def getHeaders = headers

  def setHeaders(headers: Headers) {
    this.headers = if (headers == null) new Headers
    else headers
  }

  def getBody = body

  def setBody(body: Body) {
    this.body = if (body == null) new Body
    else body
  }

  def getRecording = recording

  def setRecording(recording: RecordingProperties) {
    this.recording = if (recording == null) new RecordingProperties
    else recording
  }

  override def toString = s"RequestProperties($recording, $path, $method, $queries, $headers, $body)"
}
