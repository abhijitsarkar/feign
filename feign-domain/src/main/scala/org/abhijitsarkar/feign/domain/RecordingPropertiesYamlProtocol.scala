package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.RecordingProperties
import org.abhijitsarkar.feign.api.persistence.{IdGenerator, RequestService}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */
object RecordingPropertiesYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(RecordingPropertiesYamlProtocol.getClass)

  implicit object RecordingPropertiesYamlFormat extends YamlFormat[RecordingProperties] {
    override def write(obj: RecordingProperties): YamlValue = ???

    override def read(yaml: YamlValue): RecordingProperties = {
      def resolveIdGenerator(s: String): Option[Class[_ <: IdGenerator]] = {
        Try(Class.forName(s)) match {
          case Success(x) if (classOf[IdGenerator].isAssignableFrom(x)) => Some(x.asSubclass(classOf[IdGenerator]))
          case _ => None
        }
      }

      def resolveRecordingService(s: String): Option[Class[_ <: RequestService]] = {
        Try(Class.forName(s)) match {
          case Success(x) if (classOf[RequestService].isAssignableFrom(x)) =>
            Some(x.asSubclass(classOf[RequestService]))
          case _ => None
        }
      }

      Try(yaml.asYamlObject.fields) match {
        case Success(fields) => {
          val disable = fields.get(YamlString("disable")) match {
            case Some(x) => x.convertTo[Option[Boolean]]
            case _ => None
          }
          val idGenerator = fields.get(YamlString("idGenerator")) match {
            case Some(x) => resolveIdGenerator(x.convertTo[String])
            case _ => None
          }
          val recordingService = fields.get(YamlString("service")) match {
            case Some(x) => resolveRecordingService(x.convertTo[String])
            case _ => None
          }
          RecordingProperties(disable, idGenerator, recordingService)
        }
        case Failure(ex) => logger.warn("Defaulting recording properties.", ex); RecordingProperties()
      }
    }
  }

}

