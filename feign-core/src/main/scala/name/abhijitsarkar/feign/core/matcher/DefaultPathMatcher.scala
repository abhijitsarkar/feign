package name.abhijitsarkar.feign.core.matcher

import java.util.Locale
import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping
import org.springframework.util.{AntPathMatcher, PathMatcher}

/**
  * @author Abhijit Sarkar
  */
class DefaultPathMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  private val pathMatcher: PathMatcher = new AntPathMatcher

  override def apply(request: Request, feignMapping: FeignMapping) = {
    val requestProperties = feignMapping.request
    val requestPath = request.path

    val path = requestProperties.path.uri
    val ignoreCase = requestProperties.path.ignoreCase.getOrElse(false)

    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase(Locale.ENGLISH) else s

    Some(path).map(maybeToLower).zip(Some(requestPath).map(maybeToLower)).exists { pair =>
      pathMatcher.`match`(pair._1, pair._2)
    }
  }
}
