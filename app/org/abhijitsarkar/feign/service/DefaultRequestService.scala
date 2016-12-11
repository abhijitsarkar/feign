package org.abhijitsarkar.feign.service

import javax.inject.Inject

import akka.actor.{Actor, Props}
import org.abhijitsarkar.feign.api.persistence.{DeleteRequest, FindRequest, RecordRequest, RequestService}
import org.abhijitsarkar.feign.dao.RequestRepository
import org.slf4j.LoggerFactory
import scala.concurrent.Future

/**
  * @author Abhijit Sarkar
  */
class DefaultRequestService @Inject()(val requestRepository: RequestRepository) extends Actor with RequestService {
  private val logger = LoggerFactory.getLogger(classOf[DefaultRequestService])

  override def receive = {
    case request: RecordRequest => create(request)
    case FindRequest(Some(id)) => sender() ! find(id)
    case FindRequest(None) => sender() ! findAll
    case DeleteRequest(Some(id)) => sender() ! delete(id)
    case DeleteRequest(None) => sender() ! deleteAll
    case _ => logger.warn("Unknown request received.")
  }

  override def create(request: RecordRequest) = requestRepository.create(request)

  override def find(id: String): Future[Option[RecordRequest]] = requestRepository.find(id)

  override def findAll: Future[Seq[RecordRequest]] = requestRepository.findAll

  override def delete(id: String): Future[Option[String]] = requestRepository.delete(id)

  override def deleteAll: Future[Option[Int]] = requestRepository.deleteAll

  object DefaultRequestService {
    def props = Props[DefaultRequestService]
  }

}
