package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.{IgnorableRequestProperties, Path}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.domain.IgnorableRequestPropertiesYamlProtocol._

object PathYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(PathYamlProtocol.getClass)

  implicit object PathYamlFormat extends YamlFormat[Path] {
    override def write(obj: Path) = ???

    override def read(yaml: YamlValue) =
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val u = fields.get(YamlString("uri")) match {
            case Some(v) => v.convertTo[Option[String]]
            case _ => None
          }

          val i = yaml.convertTo[Option[IgnorableRequestProperties]]

          Path(i, u)
        }
        case Failure(ex) => logger.warn("Defaulting path.", ex); Path()
      }
  }

}
