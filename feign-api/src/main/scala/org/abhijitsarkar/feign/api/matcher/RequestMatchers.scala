package org.abhijitsarkar.feign.api.matcher

/**
  * @author Abhijit Sarkar
  */
trait RequestMatchers {
  def getMatchers: Seq[RequestMatcher]
}
