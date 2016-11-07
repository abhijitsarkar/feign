package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.DefaultYamlProtocol
import org.abhijitsarkar.feign.api.domain.{Body, Headers, ResponseProperties}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */
object ResponsePropertiesYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(ResponsePropertiesYamlProtocol.getClass)

  import BodyYamlProtocol._
  import HeadersYamlProtocol._
  import net.jcazevedo.moultingyaml._

  implicit object ReponsePropertiesYamlFormat extends YamlFormat[ResponseProperties] {
    override def write(obj: ResponseProperties): YamlValue = ???

    override def read(yaml: YamlValue): ResponseProperties = {
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val status = fields.get(YamlString("status")) match {
            case Some(YamlNumber(x)) => x.asInstanceOf[Int]
            case _ => 200
          }
          val headers = fields.get(YamlString("headers")) match {
            case Some(x) => x.convertTo[Option[Headers]]
            case _ => None
          }
          val body = fields.get(YamlString("body")) match {
            case Some(x) => x.convertTo[Option[Body]]
            case _ => None
          }

          ResponseProperties(status, headers, body)
        }
        case Failure(ex) => logger.warn("Defaulting response properties.", ex); ResponseProperties()
      }
    }
  }

}
