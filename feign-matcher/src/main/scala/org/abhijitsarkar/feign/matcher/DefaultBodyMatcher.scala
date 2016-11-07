package org.abhijitsarkar.feign.matcher

import org.abhijitsarkar.feign.api.domain.{FeignMapping, FeignProperties}
import org.abhijitsarkar.feign.api.model.Request
import org.abhijitsarkar.feign.api.matcher.RequestMatcher

/**
  * @author Abhijit Sarkar
  */
class DefaultBodyMatcher extends RequestMatcher {
  override def apply(request: Request, feignMapping: FeignMapping, feignProperties: FeignProperties): Boolean = {
    val requestProperties = feignMapping.request
    val body = requestProperties.body
    val globalIgnorableRequestProperties = feignProperties.ignorableRequestProperties
    val ignorableRequestProperties = body.ignorableRequestProperties
      .getOrElse(globalIgnorableRequestProperties)

    val ignoreUnknown = ignorableRequestProperties.ignoreUnknown
      .orElse(globalIgnorableRequestProperties.ignoreUnknown)
      .get
    val ignoreEmpty = ignorableRequestProperties.ignoreEmpty
      .orElse(globalIgnorableRequestProperties.ignoreEmpty)
      .get
    val ignoreCase = ignorableRequestProperties.ignoreCase
      .orElse(globalIgnorableRequestProperties.ignoreCase)
      .get

    val content = body.content

    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    val requestBody = request.body
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
