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
import org.slf4j.LoggerFactory
import org.springframework.util.{AntPathMatcher, PathMatcher}

/**
  * @author Abhijit Sarkar
  */
class DefaultPathMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  val logger = LoggerFactory.getLogger(classOf[DefaultPathMatcher])

  private val pathMatcher: PathMatcher = new AntPathMatcher

  override def apply(request: Request, feignMapping: FeignMapping) = {
    val requestProperties = feignMapping.request
    val requestPath = request.path

    val path = requestProperties.path.uri
    val ignoreCase = requestProperties.path.ignoreCaseToScala.getOrElse(false)

    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase(Locale.ENGLISH) else s

    val m = Some(path).map(maybeToLower).zip(Some(requestPath).map(maybeToLower)).exists { pair =>
      pathMatcher.`match`(pair._1, pair._2)
    }

    logger.info(s"Comparing request path: ${requestPath} with uri: ${path}.")
    logger.info(s"Path match returned: ${m}.")

    m
  }
}
