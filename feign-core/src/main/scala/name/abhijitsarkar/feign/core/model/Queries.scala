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

import java.util.{Collections, List => JavaList, Map => JavaMap}

import scala.collection.JavaConverters._


/**
  * @author Abhijit Sarkar
  */
class Queries extends AbstractIgnorableRequestProperties {
  var pairs: JavaMap[String, JavaList[String]] = _
  setPairs(null)

  def pairsToScala = pairs.asScala.mapValues(_.asScala.toList).toMap

  def getPairs = pairs

  def setPairs(pairs: JavaMap[String, JavaList[String]]) {
    this.pairs = if (pairs == null) Collections.emptyMap()
    else pairs
  }

  override def toString = s"Queries($pairs)${super.toString}"
}
