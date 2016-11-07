package org.abhijitsarkar.feign.domain

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.model.Request
import org.abhijitsarkar.feign.api.persistence.RecordRequest
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import scala.collection.immutable.{Map => ImmutableMap}

object RequestFormat {
  val requestRead: Reads[Request] =
    (
      (JsPath \ "path").read[String] and
        (JsPath \ "method").read[String] and
        (JsPath \ "queries").read[ImmutableMap[String, Seq[String]]] and
        (JsPath \ "headers").read[ImmutableMap[String, String]] and
        (JsPath \ "body").readNullable[String]
      ) (Request.apply _)

  val requestWrite: Writes[Request] =
    (
      (JsPath \ "path").write[String] and
        (JsPath \ "method").write[String] and
        (JsPath \ "queries").write[ImmutableMap[String, Seq[String]]] and
        (JsPath \ "headers").write[ImmutableMap[String, String]] and
        (JsPath \ "body").writeNullable[String]
      ) (unlift(Request.unapply))

  implicit val requestFormat: Format[Request] = Format(requestRead, requestWrite)

  val recordRequestRead: Reads[RecordRequest] =
    (
      (JsPath \ "request").read[Request] and
        (JsPath \ "id").read[String]
      ) (RecordRequest.apply _)

  val recordRequestWrite: Writes[RecordRequest] =
    (
      (JsPath \ "request").write[Request] and
        (JsPath \ "id").write[String]
      ) (unlift(RecordRequest.unapply))

  implicit val recordRequestFormat: Format[RecordRequest] = Format(recordRequestRead, recordRequestWrite)
}
