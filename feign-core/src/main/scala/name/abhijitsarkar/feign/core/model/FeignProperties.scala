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

package name.abhijitsarkar.feign.core.model

import java.lang.{Boolean => JavaBoolean}
import java.util.{Collections, List => JavaList}
import javax.annotation.PostConstruct

import name.abhijitsarkar.feign.persistence.DefaultIdGenerator
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
@Component
@ConfigurationProperties(prefix = "feign")
class FeignProperties extends AbstractIgnorableRequestProperties {
  var recording: RecordingProperties = _
  var mappings: JavaList[FeignMapping] = _

  @PostConstruct
  def postConstruct() {
    setMappings(mappings)
    setRecording(recording)
    setIgnoreCase(null.asInstanceOf[JavaBoolean])
    setIgnoreUnknown(null.asInstanceOf[JavaBoolean])
    setIgnoreEmpty(null.asInstanceOf[JavaBoolean])
  }

  def mappingsToScala = mappings.asScala

  def getMappings = mappings

  def setMappings(mappings: JavaList[FeignMapping]) {
    this.mappings = if (mappings == null) Collections.emptyList()
    else mappings
  }

  def getRecording = recording

  def setRecording(recording: RecordingProperties) {
    this.recording = if (recording == null) new RecordingProperties
    else recording

    if (this.recording.getIdGenerator == null)
      this.recording.setIdGenerator(classOf[DefaultIdGenerator])
  }

  override def setIgnoreCase(ignoreCase: JavaBoolean) {
    super.setIgnoreCase(if (ignoreCase == null) JavaBoolean.FALSE else ignoreCase)
  }

  override def setIgnoreUnknown(ignoreUnknown: JavaBoolean) {
    super.setIgnoreUnknown(if (ignoreUnknown == null) JavaBoolean.TRUE else ignoreUnknown)
  }

  override def setIgnoreEmpty(ignoreEmpty: JavaBoolean) {
    super.setIgnoreEmpty(if (ignoreEmpty == null) JavaBoolean.TRUE else ignoreEmpty)
  }

  override def toString = s"FeignProperties($recording, $mappings)"
}
