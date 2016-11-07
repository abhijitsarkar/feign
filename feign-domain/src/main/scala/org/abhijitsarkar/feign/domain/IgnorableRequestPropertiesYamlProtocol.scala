package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.IgnorableRequestProperties
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */
object IgnorableRequestPropertiesYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(IgnorableRequestPropertiesYamlProtocol.getClass)

  implicit object IgnorableRequestPropertiesYamlFormat extends YamlFormat[IgnorableRequestProperties] {
    override def write(obj: IgnorableRequestProperties): YamlValue = ???

    override def read(yaml: YamlValue): IgnorableRequestProperties = {
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val iu = fields.get(YamlString("ignoreUnknown")).flatMap(_.convertTo[Option[Boolean]])
          val ie = fields.get(YamlString("ignoreEmpty")).flatMap(_.convertTo[Option[Boolean]])
          val ic = fields.get(YamlString("ignoreCase")).flatMap(_.convertTo[Option[Boolean]])

          IgnorableRequestProperties(iu, ie, ic)
        }
        case Failure(ex) => logger.warn("Defaulting ignorable request properties."); IgnorableRequestProperties()
      }
    }
  }

}