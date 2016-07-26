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

import org.springframework.core.io.{ClassPathResource, UrlResource}
import org.springframework.util.StringUtils._

import scala.beans.BeanProperty
import scala.io.Source
import scala.util.{Success, Try}

/**
  * @author Abhijit Sarkar
  */
class Body extends AbstractIgnorableRequestProperties {
  @BeanProperty
  var raw: String = _
  @BeanProperty
  var url: String = _
  @BeanProperty
  var classpath: String = _

  def content = getContent

  def getContent: Option[String] = {
    validate()

    if (!isEmpty(raw)) Some(raw)
    else if (!isEmpty(url) || !isEmpty(classpath)) {
      val resource = List(url, classpath).filterNot(isEmpty _).head
      Try(new ClassPathResource(resource).getInputStream()) match {
        case Success(is) => Some(Source.fromInputStream(is).mkString)
        case _ => {
          Try(new UrlResource(resource).getInputStream()) match {
            case Success(is) => Some(Source.fromInputStream(is).mkString)
            case _ => None
          }
        }
      }
    } else None
  }

  private def validate() {
    val count = List(raw, url, classpath).filterNot(isEmpty).size

    assert(count <= 1, "Ambiguous request body declaration.")
  }

  override def toString = s"Body($raw, $url, $classpath)"
}
