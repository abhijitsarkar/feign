package org.abhijitsarkar.feign.matcher

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.domain.{FeignMapping, FeignProperties}
import org.abhijitsarkar.feign.api.matcher.RequestMatcher
import org.abhijitsarkar.feign.api.model.Request
import org.slf4j.LoggerFactory

/**
  * @author Abhijit Sarkar
  */
class DefaultQueriesMatcher extends RequestMatcher {
  val logger = LoggerFactory.getLogger(classOf[DefaultQueriesMatcher])

  override def apply(request: Request, feignMapping: FeignMapping, feignProperties: FeignProperties): Boolean = {
    val requestProperties = feignMapping.request
    val queries = requestProperties.queries
    val globalIgnorableRequestProperties = feignProperties.ignorableRequestProperties
    val ignorableRequestProperties = queries.ignorableRequestProperties
      .getOrElse(globalIgnorableRequestProperties)

    val ignoreUnknown = ignorableRequestProperties.ignoreUnknown
      .orElse(globalIgnorableRequestProperties.ignoreUnknown)
      .get
    val pairs = queries.pairs
    val queryParams = request.queries

    if (!queryParams.isEmpty && pairs.isEmpty && !ignoreUnknown) {
      logger.info("Query params are not empty but property queries are, " +
        "and ignoreUnknown is false. Queries match returned false.")

      return false
    }

    val ignoreEmpty = ignorableRequestProperties.ignoreEmpty
      .orElse(globalIgnorableRequestProperties.ignoreEmpty)
      .get

    if (queryParams.isEmpty) {
      val m = ignoreEmpty || pairs.isEmpty
      logger.info(s"Query params are empty. Queries match returned ${m}.")

      return m
    }

    val ignoreCase = ignorableRequestProperties.ignoreCase
      .orElse(globalIgnorableRequestProperties.ignoreCase)
      .get
    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    queryParams.keys.map(k => (queryParams(k), pairs.getOrElse(k, Nil))).foldLeft(true) { (acc, pair) =>
      acc && (pair match {
        case (_, Nil) => ignoreUnknown
        case (l1, l2) => l1.map(maybeToLower).sorted.zip(l2.map(maybeToLower).sorted).forall { p =>
          logger.info(s"Comparing query param [${p._1}] with [${p._2}].")

          p._1.matches(p._2)
        }
      })
    }
  }
}
