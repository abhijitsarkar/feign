package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.{Headers, IgnorableRequestProperties}
import org.slf4j.LoggerFactory

import scala.collection.immutable.{Map => ImmutableMap}
import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.domain.IgnorableRequestPropertiesYamlProtocol._

object HeadersYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(HeadersYamlProtocol.getClass)

  implicit object HeadersYamlFormat extends YamlFormat[Headers] {
    override def write(obj: Headers): YamlValue = ???

    override def read(yaml: YamlValue): Headers = {
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val p = fields.get(YamlString("pairs")) match {
            case Some(v) => v.convertTo[Map[String, String]]
            case _ => ImmutableMap[String, String]()
          }

          val i = yaml.convertTo[Option[IgnorableRequestProperties]]

          Headers(i, p)
        }
        case Failure(ex) => logger.warn("Defaulting headers.", ex); Headers()
      }
    }
  }

}

