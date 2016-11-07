package org.abhijitsarkar.feign.controllers

import javax.inject.{Inject, Named, Singleton}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.abhijitsarkar.feign.api.model.{Request => RequestPojo}
import org.abhijitsarkar.feign.api.persistence.{DeleteRequest, FindRequest}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

/**
  * @author Abhijit Sarkar
  */

@Singleton
class RequestController @Inject()(@Named("requestService") val requestService: ActorRef) extends Controller {

  import org.abhijitsarkar.feign.domain.RequestFormat._

  import scala.concurrent.duration._

  implicit val timeout: Timeout = 5.seconds

  def find(id: String) = Action.async { implicit request =>
    for {
      future <- (requestService ? FindRequest(Some(id))).mapTo[Future[Option[RequestPojo]]]
      response <- future
      result = response.map(x => Ok(Json.toJson(x))).getOrElse(NotFound)
    } yield result
  }

  def findAll() = Action.async { implicit request =>
    for {
      response <- (requestService ? FindRequest(None)).mapTo[Future[Seq[RequestPojo]]]
      result <- response.map(x => if (x.isEmpty) NotFound else Ok(Json.toJson(x)))
    } yield result
  }

  def delete(id: String) = Action.async { implicit request =>
    for {
      future <- (requestService ? DeleteRequest(Some(id))).mapTo[Future[Option[String]]]
      response <- future
      result = response.map(x => Ok(Json.toJson(x))).getOrElse(NotFound)
    } yield result
  }

  def deleteAll() = Action.async { implicit request =>
    for {
      response <- (requestService ? DeleteRequest(None)).mapTo[Future[Option[Int]]]
      result <- response.map(x => if (x.isEmpty) NotFound else Ok(Json.toJson(x)))
    } yield result
  }

}

