package org.abhijitsarkar.feign.api.persistence

import org.abhijitsarkar.feign.api.model.Request

import scala.concurrent.Future

/**
  * @author Abhijit Sarkar
  */
trait RequestService {
  def create(request: RecordRequest): Future[Option[String]]

  def find(id: String): Future[Option[RecordRequest]]

  def findAll: Future[Seq[RecordRequest]]

  def delete(id: String): Future[Option[String]]

  def deleteAll: Future[Option[Int]]
}
