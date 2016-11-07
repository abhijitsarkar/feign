package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.DefaultYamlProtocol
import org.abhijitsarkar.feign.api.domain.{FeignMapping, RequestProperties, ResponseProperties}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.domain.FeignMapping._

object FeignMappingYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(FeignMappingYamlProtocol.getClass)

  import RequestPropertiesYamlProtocol._
  import ResponsePropertiesYamlProtocol._
  import net.jcazevedo.moultingyaml._

  implicit object FeignMappingYamlFormat extends YamlFormat[FeignMapping] {
    override def write(obj: FeignMapping): YamlValue = ???

    override def read(yaml: YamlValue): FeignMapping = {
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val req = fields.get(YamlString("request")) match {
            case Some(x) => x.convertTo[RequestProperties]
            case _ => defaultRequestProperties
          }
          val res = fields.get(YamlString("responses")) match {
            case Some(x) => x.convertTo[Seq[ResponseProperties]]
            case _ => defaultResponseProerties
          }

          FeignMapping(req, res)
        }
        case Failure(ex) => logger.warn("Defaulting Feign mapping.", ex); FeignMapping()
      }
    }
  }

}
