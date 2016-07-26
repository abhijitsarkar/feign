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

import java.lang.{Boolean => JavaBoolean}

/**
  * @author Abhijit Sarkar
  */
class Method {
  var name: String = _
  var ignoreCase: JavaBoolean = _

  setName(null)
  setIgnoreCase(null)

  def getName = name

  import Method.WILDCARD

  def setName(name: String) {
    this.name = if (name == null) WILDCARD
    else name
  }

  def ignoreCaseToScala: Option[Boolean] = Option(ignoreCase)

  def getIgnoreCase = ignoreCase

  def setIgnoreCase(ignoreCase: JavaBoolean) {
    this.ignoreCase = if (ignoreCase == null) JavaBoolean.FALSE else ignoreCase
  }

  override def toString = s"Method($name, $ignoreCase)"
}

object Method {
  val WILDCARD = ".*"
}
