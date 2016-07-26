/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 */

package name.abhijitsarkar.feign.core.service

import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.matcher._
import name.abhijitsarkar.feign.core.model.{FeignMapping, FeignProperties, RequestProperties}
import name.abhijitsarkar.feign.persistence.{IdGenerator, RecordingRequest}
import org.mockito.{ArgumentCaptor, Mockito}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.springframework.context.ApplicationEventPublisher

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class FeignServiceSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val request = new Request
  request.path = "/a"
  request.method = "GET"

  val matchers = List(new DefaultPathMatcher, new DefaultMethodMatcher, new DefaultQueriesMatcher,
    new DefaultHeadersMatcher, new DefaultBodyMatcher)

  var feignProperties: FeignProperties = _
  var feignMapping: FeignMapping = _
  var feignService: FeignService = _
  var eventPublisher: ApplicationEventPublisher = _

  before {
    feignProperties = new FeignProperties()
    feignProperties.postConstruct()
    feignMapping = new FeignMapping()
    feignProperties.mappings = List(feignMapping).asJava

    eventPublisher = Mockito.mock(classOf[ApplicationEventPublisher])

    feignService = new FeignService(feignProperties, eventPublisher, matchers.asJavaCollection)
  }

  "feign service" should "find mapping and publish event using given id generator" in {
    val requestProperties = new RequestProperties
    requestProperties.recording.idGenerator = classOf[TestIdGenerator]
    feignMapping.request = requestProperties

    val mapping = feignService.findFeignMapping(request)

    mapping shouldBe defined

    val argCaptor = ArgumentCaptor.forClass(classOf[RecordingRequest])

    Mockito.verify(eventPublisher).publishEvent(argCaptor.capture())

    val arg = argCaptor.getValue.asInstanceOf[RecordingRequest]

    arg.id shouldEqual ("1")
  }

  it should "mapping and publish event using global id generator" in {
    val mapping = feignService.findFeignMapping(request)

    mapping shouldBe defined

    val argCaptor = ArgumentCaptor.forClass(classOf[RecordingRequest])

    Mockito.verify(eventPublisher).publishEvent(argCaptor.capture())

    val arg = argCaptor.getValue.asInstanceOf[RecordingRequest]

    arg.id startsWith ("a-")
  }

  it should "find mapping but not publish event as per local recording disabled" in {
    feignProperties.recording.disable = false
    feignMapping.request.recording.disable = true

    val mapping = feignService.findFeignMapping(request)

    mapping shouldBe defined

    import org.mockito.ArgumentMatchers.any
    Mockito.verify(eventPublisher, Mockito.never()).publishEvent(any())
  }

  it should "find mapping and publish event as per local recording enabled" in {
    feignProperties.recording.disable = true
    feignMapping.request.recording.disable = false

    val mapping = feignService.findFeignMapping(request)

    mapping shouldBe defined

    val argCaptor = ArgumentCaptor.forClass(classOf[RecordingRequest])

    Mockito.verify(eventPublisher).publishEvent(argCaptor.capture())

    val arg = argCaptor.getValue.asInstanceOf[RecordingRequest]

    arg.id startsWith ("a-")
  }

  it should "not find mapping but publish event using global generator" in {
    feignService.matchers = List(new NoMatchMatcher).asJavaCollection
    feignProperties.recording.disable = false

    val mapping = feignService.findFeignMapping(request)

    mapping shouldBe empty

    val argCaptor = ArgumentCaptor.forClass(classOf[RecordingRequest])

    Mockito.verify(eventPublisher).publishEvent(argCaptor.capture())

    val arg = argCaptor.getValue.asInstanceOf[RecordingRequest]

    arg.id startsWith ("a-")
  }

  it should "not find mapping and not publish event as per recording disabled" in {
    feignService.matchers = List(new NoMatchMatcher).asJavaCollection
    feignProperties.recording.disable = true

    val mapping = feignService.findFeignMapping(request)

    mapping shouldBe empty

    import org.mockito.ArgumentMatchers.any
    Mockito.verify(eventPublisher, Mockito.never()).publishEvent(any())
  }
}

class TestIdGenerator extends IdGenerator {
  override def id(request: Request) = "1"
}

class NoMatchMatcher extends BiFunction[Request, FeignMapping, Boolean] {
  override def apply(request: Request, feignMapping: FeignMapping) =
    false
}
