package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlNumber, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.{Retry, RetryStrategy}
import org.slf4j.LoggerFactory

import scala.util.{Success, Try}

/**
  * @author Abhijit Sarkar
  */

object RetryYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(RetryYamlProtocol.getClass)

  implicit object RetryYamlFormat extends YamlFormat[Retry] {
    override def write(obj: Retry): YamlValue = ???

    override def read(yaml: YamlValue): Retry = {
      def resolveRetryStrategy(s: String): Option[String] = {
        Try(Option(s).map(RetryStrategy.withName)) match {
          case Success(x) => x.map(_.toString)
          case _ => None
        }
      }

      Try(yaml.asYamlObject.getFields(
        YamlString("maxRetryCount"),
        YamlString("retryStrategy")
      ) match {
        case Seq(YamlNumber(x), YamlString(y)) => Retry(Some(x.asInstanceOf[Int]), resolveRetryStrategy(y))
        case Seq(YamlNumber(x)) => Retry(maxRetryCount = Some(x.asInstanceOf[Int]), None)
        case Seq(YamlString(x)) => Retry(retryStrategy = resolveRetryStrategy(x))
      }).recoverWith { case ex => logger.warn("Defaulting retry.", ex); throw ex }
        .getOrElse(Retry())
    }
  }

}
