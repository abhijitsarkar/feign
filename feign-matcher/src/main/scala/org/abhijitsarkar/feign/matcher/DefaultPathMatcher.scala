package org.abhijitsarkar.feign.matcher

import io.github.azagniotov.AntPathMatcher
import org.abhijitsarkar.feign.api.domain.{FeignMapping, FeignProperties}
import org.abhijitsarkar.feign.api.matcher.RequestMatcher
import org.abhijitsarkar.feign.api.model.Request
import org.slf4j.LoggerFactory

/**
  * @author Abhijit Sarkar
  */
class DefaultPathMatcher extends RequestMatcher {
  val logger = LoggerFactory.getLogger(classOf[DefaultPathMatcher])

  private val pathMatcher = new AntPathMatcher.Builder().build()

  override def apply(request: Request, feignMapping: FeignMapping, feignProperties: FeignProperties): Boolean = {
    val requestProperties = feignMapping.request
    val path = requestProperties.path
    val globalIgnorableRequestProperties = feignProperties.ignorableRequestProperties
    val ignorableRequestProperties = path.ignorableRequestProperties
      .getOrElse(globalIgnorableRequestProperties)

    val ignoreUnknown = ignorableRequestProperties.ignoreUnknown
      .orElse(globalIgnorableRequestProperties.ignoreUnknown)
      .get

    val uri = path.uri

    if (uri.isEmpty && !ignoreUnknown) {
      logger.info("Property path URI is empty, and ignoreUnknown is false. Path match returned false.")

      return false
    }

    val ignoreCase = ignorableRequestProperties.ignoreCase
      .orElse(globalIgnorableRequestProperties.ignoreCase)
      .get

    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    val requestPath = request.path
    val m = uri.map(maybeToLower).zip(Some(requestPath).map(maybeToLower)).exists(x => pathMatcher.isMatch(x._1, x._2))

    logger.info(s"Comparing request path: ${requestPath} with uri: ${path}.")
    logger.info(s"Path match returned: ${m}.")

    m
  }
}
