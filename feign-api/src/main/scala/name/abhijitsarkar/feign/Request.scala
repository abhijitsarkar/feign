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

package name.abhijitsarkar.feign

import scala.beans.BeanProperty
import scala.collection.immutable.{List => ImmutableList, Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class Request {
  @BeanProperty
  var path: String = "/**"
  @BeanProperty
  var method: String = "GET"
  @BeanProperty
  var queryParams: Map[String, ImmutableList[String]] = ImmutableMap()
  @BeanProperty
  var headers: Map[String, String] = ImmutableMap()
  @BeanProperty
  var body: Option[String] = None
}
