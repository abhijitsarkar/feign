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

import java.util.{Collections, Objects, Optional, List => JavaList, Map => JavaMap}

import name.abhijitsarkar.feign.Request
import org.springframework.data.annotation.Id

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class RecordingRequest {
  @Id
  @BeanProperty
  var id: String = _
  @BeanProperty
  var path: String = "/**"
  @BeanProperty
  var method: String = "GET"
  @BeanProperty
  var queryParams: JavaMap[String, JavaList[String]] = Collections.emptyMap()
  @BeanProperty
  var headers: JavaMap[String, String] = Collections.emptyMap()
  @BeanProperty
  var body: String = _

  def copyFrom(request: Request): RecordingRequest = {
    this.path = request.path
    this.method = request.method
    this.queryParams = request.queryParams.mapValues(_.asJava).asJava
    this.headers = request.headers.asJava
    this.body = request.body.getOrElse(null)

    this
  }

  override def equals(that: Any): Boolean =
    that match {
      case t: RecordingRequest => t.id == id
      case _ => false
    }

  override def hashCode: Int = Objects.hash(id)
}