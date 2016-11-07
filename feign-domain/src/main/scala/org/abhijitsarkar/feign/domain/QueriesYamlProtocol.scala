package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.{IgnorableRequestProperties, Queries}
import org.slf4j.LoggerFactory

import scala.collection.immutable.{Map => ImmutableMap}
import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.domain.IgnorableRequestPropertiesYamlProtocol._

object QueriesYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(QueriesYamlProtocol.getClass)

  implicit object QueriesYamlFormat extends YamlFormat[Queries] {
    override def write(obj: Queries): YamlValue = ???

    override def read(yaml: YamlValue): Queries = {
      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val p = fields.get(YamlString("pairs")) match {
            case Some(v) => v.convertTo[Map[String, Seq[String]]]
            case _ => ImmutableMap[String, Seq[String]]()
          }

          val i = yaml.convertTo[Option[IgnorableRequestProperties]]

          Queries(i, p)
        }
        case Failure(ex) => logger.warn("Defaulting queries.", ex); Queries()
      }
    }
  }

}

