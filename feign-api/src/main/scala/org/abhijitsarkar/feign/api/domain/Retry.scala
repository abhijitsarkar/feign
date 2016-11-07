package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */

object RetryStrategy extends Enumeration {
  val Always = Value("ALWAYS")
  val OnFailure = Value("ON_FAILURE")
}

case class Retry(maxRetryCount: Option[Int] = None, retryStrategy: Option[String] = None)
