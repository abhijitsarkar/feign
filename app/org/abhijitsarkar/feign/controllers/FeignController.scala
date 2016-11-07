package org.abhijitsarkar.feign.controllers

import javax.inject.{Inject, Named, Singleton}

import akka.actor.ActorRef
import akka.util.{ByteString, Timeout}
import org.abhijitsarkar.feign.api.domain.ResponseProperties
import org.abhijitsarkar.feign.api.model.Request
import play.api.http.{ContentTypes, HttpEntity}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.collection.immutable.{Map => ImmutableMap}
import scala.concurrent.Future

/**
  * @author Abhijit Sarkar
  */
@Singleton
class FeignController @Inject()(@Named("feignService") val feignService: ActorRef) extends Controller {

  import akka.pattern.ask

  import scala.concurrent.duration._

  implicit val timeout: Timeout = 5.minutes

  def all(whatever: String) = Action.async { implicit request =>
    val b = request.body.asText
    val h = request.headers.toSimpleMap
    val q = request.queryString
    val p = request.path
    val m = request.method

    val feignRequest = Request(path = p, method = m, headers = h, queries = q, body = b)

    for {
      future <- (feignService ? feignRequest).mapTo[Future[Option[ResponseProperties]]]
      response <- future

      result = response.map { rp =>
        val s = rp.status
        val h = rp.headers
          .map(_.pairs)
          .getOrElse(ImmutableMap[String, String]())
        val b = rp.body
          .flatMap(_.content)
          .map(x => HttpEntity.Strict(ByteString(x), Some(ContentTypes.JSON)))
          .getOrElse(HttpEntity.NoEntity)

        Result(ResponseHeader(status = s, headers = h), b)
      }
        .getOrElse(NotFound)
    } yield result
  }

}
