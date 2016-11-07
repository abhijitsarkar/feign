package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.{IgnorableRequestProperties, Method}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.domain.IgnorableRequestPropertiesYamlProtocol._

object MethodYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(MethodYamlProtocol.getClass)

  implicit object MethodYamlFormat extends YamlFormat[Method] {
    override def write(obj: Method) = ???

    override def read(yaml: YamlValue) = Try(yaml.asYamlObject.fields) match {
      case Success(fields) => {
        val u = fields.get(YamlString("name")) match {
          case Some(v) => v.convertTo[Option[String]]
          case _ => None
        }

        val i = yaml.convertTo[Option[IgnorableRequestProperties]]

        Method(i, u)
      }
      case Failure(ex) => logger.warn("Defaulting method.", ex); Method()
    }
  }

}


