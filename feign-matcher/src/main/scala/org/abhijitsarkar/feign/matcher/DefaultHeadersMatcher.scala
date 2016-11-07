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
class DefaultHeadersMatcher extends RequestMatcher {
  val logger = LoggerFactory.getLogger(classOf[DefaultHeadersMatcher])

  override def apply(request: Request, feignMapping: FeignMapping, feignProperties: FeignProperties): Boolean = {
    val requestProperties = feignMapping.request
    val headers = requestProperties.headers
    val pairs = headers.pairs
    val globalIgnorableRequestProperties = feignProperties.ignorableRequestProperties
    val ignorableRequestProperties = headers.ignorableRequestProperties
      .getOrElse(globalIgnorableRequestProperties)

    val ignoreUnknown = ignorableRequestProperties.ignoreUnknown
      .orElse(globalIgnorableRequestProperties.ignoreUnknown)
      .get

    val requestHeaders = request.headers

    if (!requestHeaders.isEmpty && pairs.isEmpty && !ignoreUnknown) {
      logger.info("Request headers are not empty but property headers are, " +
        "and ignoreUnknown is false. Headers match returned false.")

      return false
    }

    val ignoreEmpty = ignorableRequestProperties.ignoreEmpty
      .orElse(globalIgnorableRequestProperties.ignoreEmpty)
      .get

    if (requestHeaders.isEmpty) {
      val m = ignoreEmpty || pairs.isEmpty
      logger.info(s"Request headers are empty. Headers match returned ${m}.")

      return m
    }

    val ignoreCase = ignorableRequestProperties.ignoreCase
      .orElse(globalIgnorableRequestProperties.ignoreCase)
      .get
    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    requestHeaders.keys.map(k => (requestHeaders(k), pairs.get(k).orElse(None))).foldLeft(true) { (acc, pair) =>
      acc && (pair match {
        case (_, None) => ignoreUnknown
        case (v1, Some(v2)) => {
          logger.info(s"Comparing request header [${v1}] with [${v2}].")
          maybeToLower(v1).matches(maybeToLower(v2))
        }
      })
    }
  }
}
