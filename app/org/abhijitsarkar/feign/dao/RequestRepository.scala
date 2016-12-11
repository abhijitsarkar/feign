package org.abhijitsarkar.feign.dao

import javax.inject.{Inject, Singleton}

import com.google.inject.ImplementedBy
import org.abhijitsarkar.feign.api.persistence.{RecordRequest, RequestService}
import org.abhijitsarkar.feign.domain.RequestFormat._
import org.slf4j.LoggerFactory
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * @author Abhijit Sarkar
  */

@ImplementedBy(classOf[MongoDbRequestRepository])
trait RequestRepository extends RequestService

@Singleton
class MongoDbRequestRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends RequestRepository {
  private val logger = LoggerFactory.getLogger(classOf[MongoDbRequestRepository])
  private val futureCollection = reactiveMongoApi.database.map(_.collection[JSONCollection]("feign"))

  private def logResult(id: Option[String], f: Future[_]) = {
    f.onComplete {
      case Success(_) => logger.debug(s"""Successfully executed database request${id.map(_ => " with id: " + id).getOrElse("")}.""")
      case Failure(ex) => logger.error(s"""Failed to execute database request${id.map(_ => " with id: " + id).getOrElse("")}.""", ex)
    }
  }

  override def findAll: Future[Seq[RecordRequest]] = {
    logger.warn(s"Attempting to find all recorded requests.")

    val result = futureCollection
      .flatMap(_.find(Json.obj()).cursor[RecordRequest](ReadPreference.primary)
        .foldWhile(Seq.empty[RecordRequest], 100)({ (acc, req) =>
          Cursor.Cont(acc :+ req)
        }, Cursor.FailOnError((a, t) => logger.error("Failed to find requests.", t))))

    logResult(None, result)

    result
  }

  override def find(id: String): Future[Option[RecordRequest]] = {
    logger.debug(s"Attempting to find recorded request for id: ${id}.")

    val result = futureCollection
      .flatMap(_.find(Json.obj("id" -> id)).one[RecordRequest])

    logResult(Some(id), result)

    result.map {
      case x@Some(_) => logger.debug(s"Successfully found request with id: ${id}."); x
      case _ => logger.warn(s"Failed to find request with id: ${id}."); None
    }
  }

  implicit val recordRequestMongoFormat = Json.format[RecordRequest]

  override def create(request: RecordRequest): Future[Option[String]] = {
    logger.debug(s"Attempting to persist request for id: ${request.id}.")

    val future = futureCollection.flatMap(_.insert[RecordRequest](request))

    logResult(Some(request.id), future)

    for {
      result <- future
      either = result.ok match {
        case true => logger.debug(s"Successfully persisted request with id. ${request.id}."); Some(request.id)
        case _ => {
          logger.error(s"Failed to persist request with id: ${request.id}. Reason: ${result.writeErrors.toString}.")
          None
        }
      }
    } yield either
  }

  override def deleteAll: Future[Option[Int]] = {
    logger.warn(s"Attempting to delete all recorded requests.")

    val future = futureCollection.flatMap(_.remove(Json.obj()))

    logResult(None, future)

    for {
      result <- future
      either = result.ok match {
        case true => logger.debug(s"Successfully deleted all requests."); Some(result.n)
        case _ => {
          logger.warn(s"Failed to delete requests. Reason: ${result.writeErrors.toString}.")
          None
        }
      }
    } yield either
  }

  override def delete(id: String): Future[Option[String]] = {
    logger.debug(s"Attempting to delete recorded request for id: ${id}.")

    val future = futureCollection.flatMap(_.remove(Json.obj("id" -> id)))

    logResult(Some(id), future)

    for {
      result <- future
      either = result.ok match {
        case true => logger.debug(s"Successfully deleted request with id. ${id}."); Some(id)
        case _ => {
          logger.warn(s"Failed to delete request with id: ${id}. Reason: ${result.writeErrors.toString}.")
          None
        }
      }
    } yield either
  }
}