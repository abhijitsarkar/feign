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
import java.util.{List => JavaList}

import name.abhijitsarkar.feign.persistence.DefaultIdGenerator
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class FeignPropertiesSpec extends FlatSpec with Matchers {
  "feign properties" should "initialize all fields" in {
    val feignProperties = new FeignProperties

    feignProperties.postConstruct

    feignProperties.getIgnoreUnknown shouldBe true
    feignProperties.getIgnoreEmpty shouldBe true
    feignProperties.getIgnoreCase shouldBe false
    feignProperties.mappings should not be null
    feignProperties.recording should not be null
  }

  it should "set null ignoreCase to false" in {
    val feignProperties = new FeignProperties

    feignProperties.setIgnoreCase((null.asInstanceOf[JavaBoolean]))
    feignProperties.getIgnoreCase shouldBe (false)
  }

  it should "set ignoreCase" in {
    val ignoreCase = List(JavaBoolean.TRUE, JavaBoolean.FALSE)
    val feignProperties = new FeignProperties

    ignoreCase.foreach { b =>
      feignProperties.setIgnoreCase(b)
      feignProperties.getIgnoreCase shouldBe (b)
    }
  }

  it should "set null ignoreUnknown to true" in {
    val feignProperties = new FeignProperties

    feignProperties.setIgnoreUnknown((null.asInstanceOf[JavaBoolean]))
    feignProperties.getIgnoreUnknown shouldBe (true)
  }

  it should "set ignoreUnknown" in {
    val ignoreUnknown = List(JavaBoolean.TRUE, JavaBoolean.FALSE)
    val feignProperties = new FeignProperties

    ignoreUnknown.foreach { b =>
      feignProperties.setIgnoreUnknown(b)
      feignProperties.getIgnoreUnknown shouldBe (b)
    }
  }

  it should "set null ignoreEmpty to true" in {
    val feignProperties = new FeignProperties

    feignProperties.setIgnoreEmpty((null.asInstanceOf[JavaBoolean]))
    feignProperties.getIgnoreEmpty shouldBe (true)
  }

  it should "set ignoreEmpty" in {
    val ignoreEmpty = List(JavaBoolean.TRUE, JavaBoolean.FALSE)
    val feignProperties = new FeignProperties

    ignoreEmpty.foreach { b =>
      feignProperties.setIgnoreEmpty(b)
      feignProperties.getIgnoreEmpty shouldBe (b)
    }
  }

  it should "set null mappings to empty" in {
    val feignProperties = new FeignProperties

    feignProperties.setMappings(null)

    feignProperties.mappings shouldBe empty
  }

  it should "set mappings" in {
    val feignProperties = new FeignProperties
    val mappings = List(new FeignMapping).asJava
    feignProperties.setMappings(mappings)

    feignProperties.mappings should not be empty
  }

  it should "set null recording to new recording" in {
    val feignProperties = new FeignProperties

    feignProperties.setRecording(null)

    feignProperties.recording should not be null
    feignProperties.recording.idGenerator shouldBe (classOf[DefaultIdGenerator])
  }

  it should "set recording" in {
    val feignProperties = new FeignProperties

    feignProperties.setRecording(new RecordingProperties)

    feignProperties.recording should not be null
    feignProperties.recording.idGenerator shouldBe (classOf[DefaultIdGenerator])
  }
}
