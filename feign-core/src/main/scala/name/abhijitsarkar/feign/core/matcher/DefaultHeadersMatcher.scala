package name.abhijitsarkar.feign.core.matcher

import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping

import scala.collection.immutable.{Iterable, Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class DefaultHeadersMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  override def apply(request: Request, feignMapping: FeignMapping): Boolean = {
    val requestProperties = feignMapping.request
    val headers = requestProperties.headers
    val pairs: ImmutableMap[String, String] = headers.pairs
    val requestHeaders: ImmutableMap[String, String] = request.headers

    val ignoreUnknown = headers.ignoreUnknown.getOrElse(false)

    val isEmpty = (it: Iterable[_]) => it == null || it.isEmpty

    if (!isEmpty(requestHeaders) && isEmpty(pairs) && !ignoreUnknown) return false

    val ignoreEmpty = headers.ignoreEmpty.getOrElse(false)

    if (isEmpty(requestHeaders)) return ignoreEmpty || isEmpty(pairs)

    val ignoreCase = headers.ignoreCase.getOrElse(false)
    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    requestHeaders.keys.map(k => (requestHeaders.get(k), pairs.get(k))).foldLeft(true) { (acc, pair) =>
      acc && (pair match {
        case (_, None) => ignoreUnknown
        case (v1, v2) => v1.map(maybeToLower).zip(v2.map(maybeToLower)).exists { p =>
          p._1.matches(p._2)
        }
      })
    }
  }
}
