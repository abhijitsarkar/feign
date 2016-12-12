package org.abhijitsarkar.feign.service

import cats.Id
import cats.data.{Kleisli, Reader}
import org.abhijitsarkar.feign.api.domain.ResponseProperties
import org.abhijitsarkar.feign.api.model.Request

/**
  * @author Abhijit Sarkar
  */

trait FeignService {
  def findResponseProperties: Kleisli[Option, Request, Seq[ResponseProperties]]

  type ResponsePropertyAndIndex = (Option[ResponseProperties], Int)

  def findResponsePropertyAndIndex: String => (Option[Seq[ResponseProperties]]) => ResponsePropertyAndIndex

  type ResponsePropertyAndDelay = (Option[ResponseProperties], Long)

  def calculateResponseDelay: String => Reader[ResponsePropertyAndIndex, Either[String, ResponsePropertyAndDelay]]

  type MessageOrResponseProperty = Either[String, Option[ResponseProperties]]

  def maybeDelayResponse: Either[String, ResponsePropertyAndDelay] => MessageOrResponseProperty

  def findFeignMapping = (id: String) => {
    findResponseProperties
      .mapF[Id, ResponsePropertyAndIndex](findResponsePropertyAndIndex(id))
      .andThen(calculateResponseDelay(id))
      .mapF[Id, MessageOrResponseProperty](maybeDelayResponse)
  }
}
