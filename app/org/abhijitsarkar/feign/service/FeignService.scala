package org.abhijitsarkar.feign.service

import cats.data.Kleisli
import cats.{Eval, Id}
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

  def calculateResponseDelay: String => Kleisli[Id, ResponsePropertyAndIndex, Either[String, ResponsePropertyAndDelay]]

  type MessageOrResponseProperty = Either[String, Option[ResponseProperties]]

  def maybeDelayResponse: Either[String, ResponsePropertyAndDelay] => MessageOrResponseProperty

  def requestId: Request => Eval[String]

  def findFeignMapping = (request: Request) => {
    val id = requestId(request)

    findResponseProperties
      .mapF[Id, ResponsePropertyAndIndex](findResponsePropertyAndIndex(id.value))
      .andThen(calculateResponseDelay(id.value))
      .mapF[Id, MessageOrResponseProperty](maybeDelayResponse)
  }
}
