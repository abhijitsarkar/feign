package org.abhijitsarkar.feign.service

import java.util.UUID
import java.util.function.BiFunction
import javax.inject.{Inject, Named}

import akka.actor.{Actor, ActorRef}
import cats.data.{Kleisli, Reader}
import cats.{Eval, Id}
import org.abhijitsarkar.feign.api.domain.{FeignProperties, ResponseProperties, RetryStrategy}
import org.abhijitsarkar.feign.api.matcher.RequestMatchers
import org.abhijitsarkar.feign.api.model.Request
import org.abhijitsarkar.feign.api.persistence.{IdGenerator, RecordRequest}
import org.slf4j.LoggerFactory
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * @author Abhijit Sarkar
  */
class FeignServiceInterpreter @Inject()(@Named("requestService") val requestService: ActorRef,
                                        val feignProperties: FeignProperties,
                                        val requestMatchers: RequestMatchers,
                                        val idGenerator: IdGenerator
                                       ) extends Actor with FeignService {
  private val logger = LoggerFactory.getLogger(classOf[FeignServiceInterpreter])

  private val requestCount = new java.util.concurrent.ConcurrentHashMap[String, Int]()
  private val failedRequestCount = new java.util.concurrent.ConcurrentHashMap[String, Int]()
  private val sum = new BiFunction[Int, Int, Int]() {
    override def apply(t: Int, u: Int): Int = t + u
  }

  override def receive = {
    case request: Request => sender() ! findFeignMapping(request)
    case _ => logger.warn("Unknown request received.")
  }

  def findFeignMapping(request: Request) = {
    Future {
      val id = requestId(request).value
      requestService ! RecordRequest(request, id)

      super.findFeignMapping(id).run(request)
    }
  }

  override val findResponseProperties = Kleisli[Option, Request, Seq[ResponseProperties]] { (request: Request) =>
    feignProperties.mappings.find(mapping => {
      requestMatchers.getMatchers.forall(matcher => {
        val m = matcher(request, mapping, feignProperties)

        logger.info(s"Matcher ${matcher.getClass.getName} returned ${m}.")

        m
      })
    })
      .map(_.responses)
  }

  override val findResponsePropertyAndIndex = (id: String) => (maybe: Option[Seq[ResponseProperties]]) => {
    val rp = maybe.getOrElse(Nil)
    val numResponses = rp.size
    val numRequests = requestCount.get(id)
    val responseIdx = {
      if (numRequests == 1 || numResponses <= 1)
        0
      else if (numResponses > 0 && numRequests % numResponses == 0)
        numResponses - 1
      else numRequests % numResponses - 1
    }

    val responseProperty = rp.zipWithIndex.find(_._2 == responseIdx).map(_._1)

    (responseProperty, responseIdx)
  }

  override val calculateResponseDelay = (id: String) =>
    Reader[ResponsePropertyAndIndex, Either[String, ResponsePropertyAndDelay]] {
      (t: ResponsePropertyAndIndex) => {
        val (responseProperty, responseIndex) = (t._1, t._2)

        val retry = feignProperties.retry
        val numFailedRequests = responseProperty.map(_.status) match {
          case Some(x) if (x >= 400) => failedRequestCount.merge(id, 1, sum)
          case _ => failedRequestCount.get(id)
        }

        val maxRetryCount = retry.maxRetryCount.get
        logger.debug(s"Max retry count: ${maxRetryCount}.")
        logger.debug(s"Retry strategy: ${retry.retryStrategy}.")

        val numRequests = requestCount.get(id)

        retry.retryStrategy.map(RetryStrategy.withName) match {
          case Some(RetryStrategy.Always) if (numRequests >= maxRetryCount) =>
            Left(s"Maximum number of retries exceeded ${maxRetryCount}.")
          case Some(RetryStrategy.OnFailure) if (numFailedRequests >= maxRetryCount) =>
            Left(s"Maximum number of failed retries exceeded ${maxRetryCount}.")
          case _ => Right((responseProperty, feignProperties.delay.effectiveDelay(responseIndex)))
        }
      }
    }

  override val maybeDelayResponse = (e: Either[String, ResponsePropertyAndDelay]) => e match {
    case Right(x) => {
      val delay = x._2
      if (delay > 0) {
        logger.warn(s"Delaying response by ${delay} millis.")
        Thread.sleep(delay)
      }
      Right(x._1)
    }
    // Stupid IntelliJ
    case x@Left(_) => x.asInstanceOf[Id[MessageOrResponseProperty]]
  }

  val requestId = (request: Request) => Eval.later {
    feignProperties.recordingProperties.disable
      .filter(_ == false)
      .map(_ => idGenerator.id(request))
      .getOrElse(UUID.randomUUID().toString)
  }
}
