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
abstract class AbstractIgnorableRequestProperties {
  private var ignoreUnknown: JavaBoolean = _
  private var ignoreEmpty: JavaBoolean = _
  private var ignoreCase: JavaBoolean = _

  def getIgnoreUnknown = ignoreUnknown

  def setIgnoreUnknown(ignoreUnknown: JavaBoolean) {
    this.ignoreUnknown = ignoreUnknown
  }

  def getIgnoreCase = ignoreCase

  def setIgnoreCase(ignoreCase: JavaBoolean) {
    this.ignoreCase = ignoreCase
  }

  def getIgnoreEmpty = ignoreEmpty

  def setIgnoreEmpty(ignoreEmpty: JavaBoolean) {
    this.ignoreEmpty = ignoreEmpty
  }

  def ignoreUnknownToScala: Option[Boolean] = Option(ignoreUnknown)

  def ignoreEmptyToScala: Option[Boolean] = Option(ignoreEmpty)

  def ignoreCaseToScala: Option[Boolean] = Option(ignoreCase)

  override def toString = s"AbstractIgnorableRequestProperties($ignoreUnknown, $ignoreEmpty, $ignoreCase)"
}
