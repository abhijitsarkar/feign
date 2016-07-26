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

import java.util.Collection
import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.{FeignMapping, FeignProperties}
import name.abhijitsarkar.feign.persistence.{IdGenerator, RecordingRequest}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import scala.language.existentials

import scala.collection.JavaConverters._
import scala.reflect.runtime.{universe => ru}

/**
  * @author Abhijit Sarkar
  */
@Service
class FeignService @Autowired()(var feignProperties: FeignProperties,
                                var eventPublisher: ApplicationEventPublisher,
                                var matchers: Collection[_ <: BiFunction[Request, FeignMapping, Boolean]]) {
  val logger = LoggerFactory.getLogger(classOf[FeignService])
  val propertiesMerger = new IgnorablePropertiesMerger

  def findFeignMapping(request: Request) = {
    val feignMapping = feignProperties.mappingsToScala.find(mapping => {
      propertiesMerger.merge(mapping.request, feignProperties)

      matchers.asScala.forall(matcher => {
        val m = matcher.apply(request, mapping)

        logger.info(s"Matcher ${matcher.getClass.getName} returned ${m}.")

        m
      })
    })

    publishEvent(request, feignMapping)

    feignMapping
  }

  def publishEvent(request: Request, feignMapping: Option[FeignMapping]) = {
    val globalRecording = feignProperties.recording

    val (idGenerator, disable) = feignMapping.map(_.request.recording)
      .map(rec => (Option(rec.idGenerator), rec.disable))
      .orElse(Some(None, globalRecording.disable))
      .map(x => (x._1.orElse(Some(globalRecording.idGenerator)), x._2))
      .map(x => (x._1.get, x._2))
      .get

    if (!disable) {
      val mirror = ru.runtimeMirror(getClass.getClassLoader)
      val cls = mirror.classSymbol(idGenerator)
      val ctr = cls.toType.decl(ru.termNames.CONSTRUCTOR).asMethod
      val cm = mirror.reflectClass(cls)
      val idGen = cm.reflectConstructor(ctr)

      /* Type info is lost at runtime */
      val id = idGen().asInstanceOf[IdGenerator].id(request)
      val recordingRequest = new RecordingRequest
      recordingRequest.copyFrom(request)
      recordingRequest.id = id

      eventPublisher.publishEvent(recordingRequest)
    }
  }
}
