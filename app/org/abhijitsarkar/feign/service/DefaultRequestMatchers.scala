package org.abhijitsarkar.feign.service

import org.abhijitsarkar.feign.api.matcher.RequestMatchers
import org.abhijitsarkar.feign.matcher._

/**
  * @author Abhijit Sarkar
  */
class DefaultRequestMatchers extends RequestMatchers {
  override val getMatchers = Seq(new DefaultPathMatcher, new DefaultMethodMatcher, new DefaultQueriesMatcher,
    new DefaultHeadersMatcher, new DefaultBodyMatcher)
}
