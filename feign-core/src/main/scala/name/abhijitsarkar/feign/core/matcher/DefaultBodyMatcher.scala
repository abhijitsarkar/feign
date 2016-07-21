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

    val ignoreUnknown = body.ignoreUnknown.getOrElse(false)
    val ignoreEmpty = body.ignoreEmpty.getOrElse(false)

    val content = body.getContent

    val maybeToLower = (s: String) => if (body.ignoreCase.getOrElse(false)) s.toLowerCase(Locale.ENGLISH) else s

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
