package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.DefaultYamlProtocol
import org.abhijitsarkar.feign.api.domain._
import org.abhijitsarkar.feign.api.matcher.RequestMatchers
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.domain.FeignProperties._

object FeignPropertiesYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(FeignPropertiesYamlProtocol.getClass)

  import DelayYamlProtocol._
  import FeignMappingYamlProtocol._
  import IgnorableRequestPropertiesYamlProtocol._
  import RecordingPropertiesYamlProtocol._
  import RetryYamlProtocol._
  import net.jcazevedo.moultingyaml._

  implicit object FeignPropertiesYamlFormat extends YamlFormat[FeignProperties] {
    override def write(obj: FeignProperties): YamlValue = ???

    override def read(yaml: YamlValue): FeignProperties = {
      def resolveRequestMatchers(s: String): Option[Class[_ <: RequestMatchers]] = {
        Try(Class.forName(s)) match {
          case Success(x) if (classOf[RequestMatchers].isAssignableFrom(x)) =>
            Some(x.asSubclass(classOf[RequestMatchers]))
          case _ => None
        }
      }

      Try(yaml.asYamlObject.fields) match {
        case Success(f) => {
          f.get(YamlString("feign")) match {
            case Some(YamlObject(fields)) => {
              val recording = fields.get(YamlString("recording")) match {
                case Some(x) => x.convertTo[RecordingProperties]
                case _ => defaultRecordingProperties
              }

              val mappings = fields.get(YamlString("mappings")) match {
                case Some(x) => x.convertTo[Seq[FeignMapping]]
                case _ => defaultFeignMapping
              }

              val delay = fields.get(YamlString("delay")) match {
                case Some(x) => x.convertTo[Delay]
                case _ => defaultDelay
              }

              val retry = fields.get(YamlString("retry")) match {
                case Some(x) => x.convertTo[Retry]
                case _ => defaultRetry
              }

              val requestMatchers = fields.get(YamlString("matchers")) match {
                case Some(x) => resolveRequestMatchers(x.convertTo[String])
                case _ => defaultRequestMatchers
              }

              // this is safe as we're already inside success case
              var ignorableRequestProperties = f.get(YamlString("feign")).get.convertTo[IgnorableRequestProperties]
              val ignoreUnknown = ignorableRequestProperties.ignoreUnknown
                .orElse(defaultIgnorableRequestProperties.ignoreUnknown)
              val ignoreEmpty = ignorableRequestProperties.ignoreEmpty
                .orElse(defaultIgnorableRequestProperties.ignoreEmpty)
              val ignoreCase = ignorableRequestProperties.ignoreCase
                .orElse(defaultIgnorableRequestProperties.ignoreCase)

              ignorableRequestProperties = IgnorableRequestProperties(ignoreUnknown, ignoreEmpty, ignoreCase)

              new FeignProperties(ignorableRequestProperties, recording, mappings,
                delay, retry, requestMatchers
              )
            }
            case _ => FeignProperties()
          }
        }
        case Failure(ex) => logger.warn("Defaulting Feign properties.", ex); FeignProperties()
      }
    }
  }

}