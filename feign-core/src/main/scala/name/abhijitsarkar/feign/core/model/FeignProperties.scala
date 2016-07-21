package name.abhijitsarkar.feign.core.model

import java.lang.{Boolean => JavaBoolean}
import java.util.{List => JavaList}
import javax.annotation.PostConstruct

import name.abhijitsarkar.feign.persistence.DefaultIdGenerator
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import scala.collection.JavaConverters._
import scala.collection.immutable.{List => ImmutableList}

/**
  * @author Abhijit Sarkar
  */
@Component
@ConfigurationProperties(prefix = "feign")
class FeignProperties extends AbstractIgnorableRequestProperties {
  var recording: RecordingProperties = _
  var mappings: ImmutableList[FeignMapping] = _

  @PostConstruct
  def postConstruct() {
    if (mappings == null) setMappings(null)
    setRecording(recording)
    setIgnoreCase(null.asInstanceOf[JavaBoolean])
    setIgnoreUnknown(null.asInstanceOf[JavaBoolean])
    setIgnoreEmpty(null.asInstanceOf[JavaBoolean])
  }

  def setMappings(mappings: JavaList[FeignMapping]) {
    this.mappings = if (mappings == null) Nil
    else mappings.asScala.toList
  }

  def setRecording(recording: RecordingProperties) {
    this.recording = if (recording == null) new RecordingProperties
    else recording

    if (this.recording.getIdGenerator == null)
      this.recording.setIdGenerator(classOf[DefaultIdGenerator])
  }

  override def toString = s"FeignProperties($recording, $mappings)"
}
