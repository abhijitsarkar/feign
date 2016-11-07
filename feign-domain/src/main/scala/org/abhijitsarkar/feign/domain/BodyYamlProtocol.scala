package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.DefaultYamlProtocol
import org.abhijitsarkar.feign.api.domain.{Body, IgnorableRequestProperties}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

object BodyYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(BodyYamlProtocol.getClass)

  import IgnorableRequestPropertiesYamlProtocol._
  import net.jcazevedo.moultingyaml._

  implicit object BodyYamlFormat extends YamlFormat[Body] {
    override def write(obj: Body): YamlValue = ???

    override def read(yaml: YamlValue): Body = {
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val raw: Option[String] = fields.get(YamlString("raw")) match {
            case Some(v) => v.convertTo[Option[String]]
            case _ => None
          }

          val url: Option[String] = fields.get(YamlString("url")) match {
            case Some(v) => v.convertTo[Option[String]]
            case _ => None
          }

          val classpath: Option[String] = fields.get(YamlString("classpath")) match {
            case Some(v) => v.convertTo[Option[String]]
            case _ => None
          }

          val ignorableRequestProperties = yaml.convertTo[Option[IgnorableRequestProperties]]

          Body(ignorableRequestProperties, raw, url, classpath)
        }
        case Failure(ex) => logger.warn("Defaulting body.", ex); Body()
      }
    }
  }

}

