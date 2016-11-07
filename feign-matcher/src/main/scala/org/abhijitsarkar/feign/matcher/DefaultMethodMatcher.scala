package org.abhijitsarkar.feign.matcher

import org.abhijitsarkar.feign.api.domain.{FeignMapping, FeignProperties}
import org.abhijitsarkar.feign.api.matcher.RequestMatcher
import org.abhijitsarkar.feign.api.model.Request
import org.slf4j.LoggerFactory

/**
  * @author Abhijit Sarkar
  */
class DefaultMethodMatcher extends RequestMatcher {
  val logger = LoggerFactory.getLogger(classOf[DefaultMethodMatcher])

  override def apply(request: Request, feignMapping: FeignMapping, feignProperties: FeignProperties): Boolean = {
    val requestProperties = feignMapping.request
    val method = requestProperties.method
    val globalIgnorableRequestProperties = feignProperties.ignorableRequestProperties
    val ignorableRequestProperties = method.ignorableRequestProperties
      .getOrElse(globalIgnorableRequestProperties)

    val ignoreUnknown = ignorableRequestProperties.ignoreUnknown
      .orElse(globalIgnorableRequestProperties.ignoreUnknown)
      .get

    val name = method.name

    if (name.isEmpty && !ignoreUnknown) {
      logger.info("Property method name is empty, and ignoreUnknown is false. Method match returned false.")

      return false
    }

    val ignoreCase = ignorableRequestProperties.ignoreCase
      .orElse(globalIgnorableRequestProperties.ignoreCase)
      .get

    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    val requestMethod = request.method
    val m = name.map(maybeToLower).zip(Some(requestMethod).map(maybeToLower)).exists(x => x._1.matches(x._2))

    logger.info(s"Comparing request method: ${requestMethod} with: ${method}.")
    logger.info(s"Method match returned ${m}.")

    m
  }
}
