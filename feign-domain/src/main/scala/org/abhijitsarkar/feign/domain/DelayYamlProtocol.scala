package org.abhijitsarkar.feign.domain

import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlNumber, YamlString, YamlValue}
import org.abhijitsarkar.feign.api.domain.{Delay, DelayStrategy}
import org.slf4j.LoggerFactory

import scala.util.{Success, Try}

/**
  * @author Abhijit Sarkar
  */

object DelayYamlProtocol extends DefaultYamlProtocol {
  val logger = LoggerFactory.getLogger(DelayYamlProtocol.getClass)

  implicit object DelayYamlFormat extends YamlFormat[Delay] {
    override def write(obj: Delay): YamlValue = ???

    override def read(yaml: YamlValue): Delay = {
      def resolveDelayStrategy(s: String): Option[String] = {
        Try(Option(s).map(DelayStrategy.withName)) match {
          case Success(x) => x.map(_.toString)
          case _ => None
        }
      }

      Try(yaml.asYamlObject.getFields(
        YamlString("delayMillis"),
        YamlString("delayStrategy")
      ) match {
        case Seq(YamlNumber(x), YamlString(y)) => new Delay(Some(x.asInstanceOf[Int].toLong), resolveDelayStrategy(y))
        case Seq(YamlNumber(x)) => new Delay(delayMillis = Some(x.asInstanceOf[Int].toLong), None)
        case Seq(YamlString(x)) => new Delay(delayStrategy = resolveDelayStrategy(x))
      }).recoverWith { case ex => logger.warn("Defaulting delay.", ex); throw ex }
        .getOrElse(Delay())
    }
  }

}
