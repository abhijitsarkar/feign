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

import name.abhijitsarkar.feign.Request

/**
  * @author Abhijit Sarkar
  */
class DefaultIdGenerator extends IdGenerator {
  val pattern = """^(?:/?)([^/]+)(?:.*)$""".r

  override def id(request: Request): String = {
    val prefix = pattern.findFirstMatchIn("/abc/xyz")
      .map(_ group 1)
      .getOrElse("unknown")

    s"${prefix}-${request.path.hashCode}"
  }
}
