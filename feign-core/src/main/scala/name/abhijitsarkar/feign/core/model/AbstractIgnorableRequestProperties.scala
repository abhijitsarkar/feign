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

package name.abhijitsarkar.feign.core.model

import java.lang.{Boolean => JavaBoolean}

import scala.beans.BeanProperty

/**
  * @author Abhijit Sarkar
  */
abstract class AbstractIgnorableRequestProperties {
  var ignoreUnknown: Option[Boolean] = _
  var ignoreEmpty: Option[Boolean] = _
  var ignoreCase: Option[Boolean] = _

  def getIgnoreUnknown = ignoreUnknown

  def getIgnoreEmpty = ignoreEmpty

  def getIgnoreCase = ignoreCase

  def setIgnoreUnknown(ignoreUnknown: Option[Boolean]) {
    this.ignoreUnknown = ignoreUnknown
  }

  def setIgnoreEmpty(ignoreEmpty: Option[Boolean]) {
    this.ignoreEmpty = ignoreEmpty
  }

  def setIgnoreCase(ignoreCase: Option[Boolean]) {
    this.ignoreCase = ignoreCase
  }

  def setIgnoreUnknown(ignoreUnknown: JavaBoolean) {
    this.ignoreUnknown = Option(ignoreUnknown)
  }

  def setIgnoreEmpty(ignoreEmpty: JavaBoolean) {
    this.ignoreEmpty = Option(ignoreEmpty)
  }

  def setIgnoreCase(ignoreCase: JavaBoolean) {
    this.ignoreCase = Option(ignoreCase)
  }

  override def toString = s"AbstractIgnorableRequestProperties($ignoreUnknown, $ignoreEmpty, $ignoreCase)"
}
