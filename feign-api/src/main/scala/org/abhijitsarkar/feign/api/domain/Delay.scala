package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */

object DelayStrategy extends Enumeration {
  val Constant = Value("CONSTANT")
  val WithLinearBackOff = Value("WITH_LINEAR_BACK_OFF")
  val WithExponentialBackOff = Value("WITH_EXPONENTIAL_BACK_OFF")
}

case class Delay(delayMillis: Option[Long] = None,
                 delayStrategy: Option[String] = None) {
  def effectiveDelay(retryCount: Int): Long = {
    // zip produces a List with a single Tuple2, or an empty List if any one Optional is None
    delayMillis.zip(delayStrategy) match {
      case List((delay, strategy)) => {
        DelayStrategy.withName(strategy) match {
          case DelayStrategy.WithLinearBackOff => delay * retryCount
          case DelayStrategy.WithExponentialBackOff => scala.math.pow(
            delay.asInstanceOf[Double], retryCount.asInstanceOf[Double]
          ).asInstanceOf[Long]
          case _ => delay
        }
      }
      case _ => 0
    }
  }
}
