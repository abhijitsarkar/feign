package name.abhijitsarkar.feign.core.matcher

import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping

/**
  * @author Abhijit Sarkar
  */
class DefaultMethodMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  override def apply(request: Request, feignMapping: FeignMapping) = {
    val requestProperties = feignMapping.request
    val requestMethod = request.method

    val method = requestProperties.method.name
    val ignoreCase = requestProperties.method.ignoreCase.getOrElse(false)

    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    Some(requestMethod).map(maybeToLower).zip(Some(method).map(maybeToLower)).exists { pair =>
      pair._1.matches(pair._2)
    }
  }
}
