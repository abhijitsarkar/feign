package org.abhijitsarkar.feign.domain

import java.io.File

import FeignPropertiesYamlProtocol._
import net.jcazevedo.moultingyaml._
import org.abhijitsarkar.feign.api.domain.FeignProperties
import org.specs2.mutable.Specification

import scala.io.Source

/**
  * @author Abhijit Sarkar
  */
class FeignPropertiesSpec extends Specification {
  "FeignProperties" should {

    "parse test file" in {
      val source = Source.fromFile(new File(getClass.getResource("/test-domain.yml").toURI)).getLines.mkString("\n")

      val yamlAst = source.parseYaml

      val feignProperties = yamlAst.convertTo[FeignProperties]

      feignProperties.requestMatchers mustEqual Some(classOf[EmptyRequestMatchers])

      feignProperties.delay.delayMillis mustEqual Some(1000)
      feignProperties.delay.delayStrategy mustEqual Some("WITH_LINEAR_BACK_OFF")

      feignProperties.retry.maxRetryCount mustEqual Some(2)
      feignProperties.retry.retryStrategy mustEqual Some("ALWAYS")

      feignProperties.recordingProperties.disable mustEqual Some(false)
      feignProperties.recordingProperties.idGenerator mustEqual Some(classOf[ConstantIdGenerator])

      feignProperties.ignorableRequestProperties.ignoreUnknown mustEqual Some(false)
      feignProperties.ignorableRequestProperties.ignoreEmpty mustEqual Some(false)
      feignProperties.ignorableRequestProperties.ignoreCase mustEqual Some(true)

      feignProperties.mappings must have size 2

      val maybeOne = feignProperties.mappings.find(_.request.path.uri == Some("/feign/abc"))

      maybeOne must not be empty

      val one = maybeOne.get

      val requestPropertiesForOne = one.request

      requestPropertiesForOne.path.uri mustEqual Some("/feign/abc")
      requestPropertiesForOne.path.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(false)

      requestPropertiesForOne.method.name mustEqual Some("GET")
      requestPropertiesForOne.method.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(false)

      requestPropertiesForOne.queries.ignorableRequestProperties.flatMap(_.ignoreUnknown) mustEqual Some(false)
      requestPropertiesForOne.queries.ignorableRequestProperties.flatMap(_.ignoreEmpty) mustEqual Some(false)
      requestPropertiesForOne.queries.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(true)
      requestPropertiesForOne.queries.pairs must havePairs("q1" -> Seq("a", "b"), "q2" -> Seq("c"))

      requestPropertiesForOne.headers.ignorableRequestProperties.flatMap(_.ignoreUnknown) mustEqual Some(false)
      requestPropertiesForOne.headers.ignorableRequestProperties.flatMap(_.ignoreEmpty) mustEqual Some(false)
      requestPropertiesForOne.headers.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(true)
      requestPropertiesForOne.headers.pairs must havePairs("h1" -> "a", "h2" -> "b")

      requestPropertiesForOne.body.ignorableRequestProperties.flatMap(_.ignoreUnknown) mustEqual Some(false)
      requestPropertiesForOne.body.ignorableRequestProperties.flatMap(_.ignoreEmpty) mustEqual Some(false)
      requestPropertiesForOne.body.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(true)
      requestPropertiesForOne.body.content mustEqual Some("body")

      one.responses must have size 2

      val maybeErrorResponseForOne = one.responses.find(_.status > 300)

      maybeErrorResponseForOne must not be empty

      val errorResponseForOne = maybeErrorResponseForOne.get
      errorResponseForOne.status mustEqual 500
      errorResponseForOne.body.flatMap(_.ignorableRequestProperties).flatMap(_.ignoreUnknown) mustEqual None
      errorResponseForOne.body.flatMap(_.ignorableRequestProperties).flatMap(_.ignoreEmpty) mustEqual None
      errorResponseForOne.body.flatMap(_.ignorableRequestProperties).flatMap(_.ignoreCase) mustEqual None
      errorResponseForOne.body.flatMap(_.content) mustEqual None
      errorResponseForOne.headers mustEqual None

      val maybeSuccessResponseForOne = one.responses.find(_.status < 300)

      maybeSuccessResponseForOne must not be empty

      val successResponseForOne = maybeSuccessResponseForOne.get
      successResponseForOne.status mustEqual 201
      successResponseForOne.body.flatMap(_.ignorableRequestProperties).flatMap(_.ignoreUnknown) mustEqual None
      successResponseForOne.body.flatMap(_.ignorableRequestProperties).flatMap(_.ignoreEmpty) mustEqual None
      successResponseForOne.body.flatMap(_.ignorableRequestProperties).flatMap(_.ignoreCase) mustEqual None
      successResponseForOne.body.flatMap(_.content) mustEqual Some("body")
      successResponseForOne.headers mustNotEqual None
      successResponseForOne.headers.get.pairs must havePairs("h3" -> "c", "h4" -> "d")
    }
  }
}
