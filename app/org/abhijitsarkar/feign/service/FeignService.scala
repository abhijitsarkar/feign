package org.abhijitsarkar.feign.service

import java.util.UUID
import java.util.function.BiFunction
import javax.inject.{Inject, Named}

import akka.actor.{Actor, ActorRef}
import org.abhijitsarkar.feign.api.domain.{FeignProperties, RecordingProperties, ResponseProperties, RetryStrategy}
import org.abhijitsarkar.feign.api.matcher.RequestMatchers
import org.abhijitsarkar.feign.api.model.Request
import org.abhijitsarkar.feign.api.persistence.{IdGenerator, RecordRequest}
import org.slf4j.LoggerFactory
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scala.util.Success

/**
  * @author Abhijit Sarkar
  */
class FeignService @Inject()(@Named("requestService") val requestService: ActorRef,
                             val feignProperties: FeignProperties,
                             val requestMatchers: RequestMatchers,
                             val idGenerator: IdGenerator
                            ) extends Actor {
  private val logger = LoggerFactory.getLogger(classOf[FeignService])

  private val requestCount = new java.util.concurrent.ConcurrentHashMap[String, Int]()
  private val failedRequestCount = new java.util.concurrent.ConcurrentHashMap[String, Int]()
  private val sum = new BiFunction[Int, Int, Int]() {
    override def apply(t: Int, u: Int): Int = t + u
  }

  override def receive = {
    case request: Request => sender() ! findFeignMapping(request)
    case _ => logger.warn("Unknown request received.")
  }

  private def getRequestId(request: Request, recordingProperties: RecordingProperties) = {
    recordingProperties.disable
      .filter(_ == false)
      .map(_ => idGenerator.id(request))
      .getOrElse(UUID.randomUUID().toString)
  }

  private def getResponseIndex(numRequests: Int, numResponse: Int) = {
    if (numRequests == 1 || numResponse <= 1)
      0
    else if (numResponse > 0 && numRequests % numResponse == 0)
      numResponse - 1
    else numRequests % numResponse - 1
  }

  private def getRetry(responseProperties: Option[ResponseProperties], numRequests: Int, id: String) = {
    val retry = feignProperties.retry
    val maxRetryCount = retry.maxRetryCount.get
    val numFailedRequests = responseProperties.map(_.status) match {
      case Some(x) if (x >= 400) => failedRequestCount.merge(id, 1, sum)
      case _ => failedRequestCount.get(id)
    }

    logger.debug(s"Max retry count: ${maxRetryCount}.")
    logger.debug(s"Retry strategy: ${retry.retryStrategy}.")

    retry.retryStrategy.map(RetryStrategy.withName) match {
      case Some(RetryStrategy.Always) if (numRequests >= maxRetryCount) => {
        Left(s"Maximum number of retries exceeded ${maxRetryCount}.")
      }
      case Some(RetryStrategy.OnFailure) if (numFailedRequests >= maxRetryCount) => {
        Left(s"Maximum number of failed retries exceeded ${maxRetryCount}.")
      }
      case _ => Right(responseProperties)
    }
  }

  private def findFeignMapping(request: Request): Future[Option[ResponseProperties]] = {
    val responseProperties = Future {
      feignProperties.mappings.find(mapping => {
        requestMatchers.getMatchers.forall(matcher => {
          val m = matcher(request, mapping, feignProperties)

          logger.info(s"Matcher ${matcher.getClass.getName} returned ${m}.")

          m
        })
      }).map(_.responses)
    }

    responseProperties.onComplete {
      case Success(rp) => logger.debug("Successfully completed request execution.")
      case _ => logger.error("Request execution failed.")
    }

    responseProperties.flatMap { x =>
      val recordingProperties = feignProperties.recordingProperties
      val id = getRequestId(request, recordingProperties)

      // publish request
      requestService ! RecordRequest(request, id)

      val rp = x.getOrElse(Nil)

      val numResponses = rp.size
      val numRequests = requestCount.merge(id, 1, sum)
      logger.debug(s"Number of requests with id: ${id} is: ${numRequests}.")

      val responseIdx = getResponseIndex(numRequests, numResponses)
      val matchingRp = rp.zipWithIndex.find(_._2 == responseIdx).map(_._1)

      val either = getRetry(matchingRp, numRequests, id)

      either match {
        case Left(msg) => Future.failed(throw new RuntimeException(msg))
        case Right(x) => {
          val delay = feignProperties.delay.effectiveDelay(responseIdx)

          logger.debug(s"Response delay for request with id: ${id} is: ${delay}.")

          if (delay > 0) {
            logger.warn(s"Delaying response by ${delay} millis.")
            Thread.sleep(delay)
          }
          Future.successful(x)
        }
      }
    }
  }
}
