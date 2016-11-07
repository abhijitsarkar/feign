package org.abhijitsarkar.feign.api.matcher

import org.abhijitsarkar.feign.api.domain.{FeignMapping, FeignProperties}
import org.abhijitsarkar.feign.api.model.Request

/**
  * @author Abhijit Sarkar
  */
trait RequestMatcher extends Function3[Request, FeignMapping, FeignProperties, Boolean]
