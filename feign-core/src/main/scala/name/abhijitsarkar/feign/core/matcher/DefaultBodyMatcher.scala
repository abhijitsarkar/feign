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

package name.abhijitsarkar.feign.core.matcher

import java.util.Locale
import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping

/**
  * @author Abhijit Sarkar
  */
class DefaultBodyMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  override def apply(request: Request, feignMapping: FeignMapping) = {
    val requestProperties = feignMapping.request
    val body = requestProperties.getBody
    val requestBody = request.getBody

    val ignoreUnknown = body.ignoreUnknownToScala.getOrElse(false)
    val ignoreEmpty = body.ignoreEmptyToScala.getOrElse(false)
    val ignoreCase: Boolean = body.ignoreCaseToScala.getOrElse(false)

    val content = body.getContent

    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase(Locale.ENGLISH) else s

    requestBody.map(maybeToLower).zip(content.map(maybeToLower)) match {
      case _ if (requestBody.isEmpty && content.isEmpty) => true
      case _ if (requestBody.isEmpty && ignoreEmpty) => true
      case _ if (!requestBody.isEmpty && content.isEmpty && ignoreUnknown) => true
      case _ if (requestBody.isEmpty) => false
      case head :: tail => head._1.matches(head._2)
      case _ => false
    }
  }
}
