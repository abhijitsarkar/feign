/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *  *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 *
 */

package name.abhijitsarkar.feign.persistence

import java.util.Objects

import name.abhijitsarkar.feign.Request
import org.springframework.data.annotation.Id

import scala.beans.BeanProperty

/**
  * @author Abhijit Sarkar
  */
class RecordingRequest extends Request {
  @Id
  @BeanProperty
  var id: String = _

  def copyFrom(request: Request): RecordingRequest = {
    this.path = request.path
    this.method = request.method
    this.queryParams = request.queryParams
    this.headers = request.headers
    this.body = request.body

    this
  }

  override def equals(that: Any): Boolean =
    that match {
      case t: RecordingRequest => t.id == id
      case _ => false
    }

  override def hashCode: Int = Objects.hash(id)
}