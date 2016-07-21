package name.abhijitsarkar.feign.core.matcher

import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping

import scala.collection.immutable.{Iterable, List => ImmutableList, Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class DefaultQueriesMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  override def apply(request: Request, feignMapping: FeignMapping): Boolean = {
    val requestProperties = feignMapping.request
    val queries = requestProperties.queries
    val ignoreUnknown = queries.ignoreUnknown.getOrElse(false)
    val pairs: ImmutableMap[String, ImmutableList[String]] = queries.pairs
    val queryParams: ImmutableMap[String, ImmutableList[String]] = request.queryParams

    val isEmpty = (it: Iterable[_]) => it == null || it.isEmpty

    if (!isEmpty(queryParams) && isEmpty(pairs) && !ignoreUnknown) return false

    val ignoreEmpty = queries.ignoreEmpty.getOrElse(false)

    if (isEmpty(queryParams)) return ignoreEmpty || isEmpty(pairs)

    val ignoreCase = queries.ignoreCase.getOrElse(false)
    val maybeToLower = (s: String) => if (ignoreCase) s.toLowerCase() else s

    queryParams.keys.map(k => (queryParams(k), pairs.getOrElse(k, Nil))).foldLeft(true) { (acc, pair) =>
      acc && (pair match {
        case (l1, Nil) => ignoreUnknown
        case (l1, l2) => l1.map(maybeToLower).sorted.zip(l2.map(maybeToLower).sorted).forall { p =>
          p._1.matches(p._2)
        }
      })
    }
  }
}
