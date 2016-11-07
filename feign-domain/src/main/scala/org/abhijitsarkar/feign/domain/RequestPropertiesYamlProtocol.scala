package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain._
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.domain.RequestProperties._
import org.abhijitsarkar.feign.domain.BodyYamlProtocol._
import org.abhijitsarkar.feign.domain.HeadersYamlProtocol._
import org.abhijitsarkar.feign.domain.MethodYamlProtocol._
import org.abhijitsarkar.feign.domain.PathYamlProtocol._
import org.abhijitsarkar.feign.domain.QueriesYamlProtocol._

object RequestPropertiesYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(RequestPropertiesYamlProtocol.getClass)

  implicit object RequestPropertiesYamlFormat extends YamlFormat[RequestProperties] {
    override def write(obj: RequestProperties): YamlValue = ???

    override def read(yaml: YamlValue): RequestProperties = {
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val path = fields.get(YamlString("path")) match {
            case Some(x) => x.convertTo[Path]
            case _ => defaultPath
          }
          val method = fields.get(YamlString("method")) match {
            case Some(x) => x.convertTo[Method]
            case _ => defaultMethod
          }
          val queries = fields.get(YamlString("queries")) match {
            case Some(x) => x.convertTo[Queries]
            case _ => defaultQueries
          }
          val headers = fields.get(YamlString("headers")) match {
            case Some(x) => x.convertTo[Headers]
            case _ => defaultHeaders
          }
          val body = fields.get(YamlString("body")) match {
            case Some(x) => x.convertTo[Body]
            case _ => defaultBody
          }

          RequestProperties(path, method, queries, headers, body)
        }
        case Failure(ex) => logger.warn("Defaulting request properties.", ex); RequestProperties()
      }
    }
  }

}
