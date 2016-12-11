package org.abhijitsarkar.feign.modules

import java.net.URL

import com.google.inject.{AbstractModule, Provides}
import net.jcazevedo.moultingyaml._
import org.abhijitsarkar.feign.api.domain.FeignProperties
import org.abhijitsarkar.feign.api.matcher.RequestMatchers
import org.abhijitsarkar.feign.api.persistence.IdGenerator
import org.abhijitsarkar.feign.domain.FeignPropertiesYamlProtocol._
import org.abhijitsarkar.feign.service.{DefaultIdGenerator, DefaultRequestMatchers, DefaultRequestService, FeignServiceInterpreter}
import org.slf4j.LoggerFactory
import play.api.libs.concurrent.AkkaGuiceSupport
import play.api.{Configuration, Environment}

import scala.io.Source
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */
class FeignModule(environment: Environment, configuration: Configuration) extends AbstractModule with AkkaGuiceSupport {
  private val logger = LoggerFactory.getLogger(classOf[FeignModule])
  private val feignProperties: FeignProperties = initFeignProperties()

  private def initFeignProperties(): FeignProperties = {
    val feignConfig = configuration.getConfig("feign").get
    val feignPropertiesPath = feignConfig.getString("propertiesUrl").get

    val fp = Try {
      val u = new URL(feignPropertiesPath)
      val con = u.openConnection()
      con.setConnectTimeout(10000)
      con.setReadTimeout(10000)
      Source.fromInputStream(con.getInputStream, "UTF-8").mkString
    } match {
      case Success(x) => Some(x)
      case Failure(ex) => logger.error(s"Failed to read from URL: ${feignPropertiesPath}.", ex); None
    }

    fp.map { x =>
      Try {
        val yamlAst = x.parseYaml

        yamlAst.convertTo[FeignProperties]
      } match {
        case Success(x) => x
        case _ => new FeignProperties
      }
    }.getOrElse(new FeignProperties)
  }

  @Provides
  def provideFeignProperties: FeignProperties = feignProperties

  @Provides
  def provideRequestMatchers: RequestMatchers = {
    feignProperties.requestMatchers
      .orElse(Some(classOf[DefaultRequestMatchers]))
      .map(_.newInstance)
      .get
  }

  @Provides
  def provideIdGenerator: IdGenerator = {
    feignProperties.recordingProperties
      .idGenerator
      .orElse(Some(classOf[DefaultIdGenerator]))
      .map(_.newInstance)
      .get
  }

  def configure(): Unit = {
    // bind user specified request service, or default
    feignProperties.recordingProperties
      .recordingService
      .orElse(Some(classOf[DefaultRequestService]))
      .foreach(x => bindActor("requestService")(ClassTag(x)))

    // bind feign service
    bindActor[FeignServiceInterpreter]("feignService")
  }
}
