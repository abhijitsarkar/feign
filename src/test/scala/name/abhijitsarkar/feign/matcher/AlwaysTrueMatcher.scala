package name.abhijitsarkar.feign.matcher

import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.FeignMapping

/**
  * @author Abhijit Sarkar
  */
class AlwaysTrueMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  override def apply(request: Request, feignMapping: FeignMapping) = true
}
