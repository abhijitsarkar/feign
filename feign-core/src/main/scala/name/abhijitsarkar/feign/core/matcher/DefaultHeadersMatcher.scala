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

import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import org.slf4j.LoggerFactory

import scala.collection.immutable.{Iterable, Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class DefaultHeadersMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  val logger = LoggerFactory.getLogger(classOf[DefaultHeadersMatcher])

  override def apply(request: Request, feignMapping: FeignMapping): Boolean = {
    val requestProperties = feignMapping.request
    val headers = requestProperties.headers
    val pairs: ImmutableMap[String, String] = headers.pairsToScala
    val requestHeaders: ImmutableMap[String, String] = request.headers

    val ignoreUnknown = headers.ignoreUnknownToScala.getOrElse(false)

    val isEmpty = (it: Iterable[_]) => it == null || it.isEmpty

    if (!isEmpty(requestHeaders) && isEmpty(pairs) && !ignoreUnknown) {
      logger.info("Request headers are not empty but request property headers are, " +
        "and ignoreUnknown is false. Headers match returned false.")

      return false
    }

    val ignoreEmpty = headers.ignoreEmptyToScala.getOrElse(false)

    if (isEmpty(requestHeaders)) {
      val m = ignoreEmpty || isEmpty(pairs)
      logger.info(s"Request headers are empty. Headers match returned {m}.")

      return m
    }

    val ignoreCase = headers.ignoreCaseToScala.getOrElse(false)
    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    requestHeaders.keys.map(k => (requestHeaders.get(k), pairs.get(k))).foldLeft(true) { (acc, pair) =>
      acc && (pair match {
        case (_, None) => {
          ignoreUnknown
        }
        case (v1, v2) => v1.map(maybeToLower).zip(v2.map(maybeToLower)).exists { p =>
          logger.info(s"Comparing request header [v1] with [v2].")

          p._1.matches(p._2)
        }
      })
    }
  }
}
