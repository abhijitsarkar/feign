package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.domain.FeignProperties._
import org.abhijitsarkar.feign.api.matcher.RequestMatchers

case class FeignProperties(ignorableRequestProperties: IgnorableRequestProperties = defaultIgnorableRequestProperties,
                           recordingProperties: RecordingProperties = defaultRecordingProperties,
                           mappings: Seq[FeignMapping] = defaultFeignMapping,
                           delay: Delay = defaultDelay,
                           retry: Retry = defaultRetry,
                           requestMatchers: Option[Class[_ <: RequestMatchers]] = defaultRequestMatchers
                          )

object FeignProperties {
  val defaultIgnorableRequestProperties = IgnorableRequestProperties(Some(true), Some(true), Some(false))

  val defaultRecordingProperties = RecordingProperties(Some(false), None)

  val defaultDelay = Delay(Some(0), Some(DelayStrategy.Constant.toString))

  val defaultFeignMapping = Seq[FeignMapping]()

  val defaultRetry = Retry(Some(Int.MaxValue), Some(RetryStrategy.Always.toString))

  val defaultRequestMatchers = None
}

