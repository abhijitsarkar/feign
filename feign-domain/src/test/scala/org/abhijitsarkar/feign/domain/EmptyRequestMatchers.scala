package org.abhijitsarkar.feign.domain

import org.abhijitsarkar.feign.api.matcher.{RequestMatcher, RequestMatchers}

/**
  * @author Abhijit Sarkar
  */
class EmptyRequestMatchers extends RequestMatchers {
  override def getMatchers: Seq[RequestMatcher] = Seq()
}
