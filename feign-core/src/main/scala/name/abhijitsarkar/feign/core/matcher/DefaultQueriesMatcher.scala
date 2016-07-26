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

import scala.collection.immutable.{Iterable, List => ImmutableList, Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class DefaultQueriesMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  val logger = LoggerFactory.getLogger(classOf[DefaultQueriesMatcher])

  override def apply(request: Request, feignMapping: FeignMapping): Boolean = {
    val requestProperties = feignMapping.request
    val queries = requestProperties.queries
    val ignoreUnknown = queries.ignoreUnknownToScala.getOrElse(false)
    val pairs: ImmutableMap[String, ImmutableList[String]] = queries.pairsToScala
    val queryParams: ImmutableMap[String, ImmutableList[String]] = request.queryParams

    val isEmpty = (it: Iterable[_]) => it == null || it.isEmpty

    if (!isEmpty(queryParams) && isEmpty(pairs) && !ignoreUnknown) {
      logger.info("Query params are not empty but request queries are, " +
        "and ignoreUnknown is false. Queries match returned false.")

      return false
    }

    val ignoreEmpty = queries.ignoreEmptyToScala.getOrElse(false)

    if (isEmpty(queryParams)) {
      val m = ignoreEmpty || isEmpty(pairs)
      logger.info(s"Query params are empty. Queries match returned ${m}.")

      return m
    }

    val ignoreCase = queries.ignoreCaseToScala.getOrElse(false)
    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    queryParams.keys.map(k => (queryParams(k), pairs.getOrElse(k, Nil))).foldLeft(true) { (acc, pair) =>
      acc && (pair match {
        case (l1, Nil) => {
          ignoreUnknown
        }
        case (l1, l2) => l1.map(maybeToLower).sorted.zip(l2.map(maybeToLower).sorted).forall { p =>
          logger.info(s"Comparing query param [l1] with [l2].")

          p._1.matches(p._2)
        }
      })
    }
  }
}
