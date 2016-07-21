package name.abhijitsarkar.feign.core.service

import java.util.function.BiFunction

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.{FeignMapping, FeignProperties}
import name.abhijitsarkar.feign.persistence.{IdGenerator, RecordingRequest}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher

import scala.reflect.runtime.{universe => ru}

/**
  * @author Abhijit Sarkar
  */
class FeignService @Autowired()(var feignProperties: FeignProperties,
                                var eventPublisher: ApplicationEventPublisher,
                                var matchers: Seq[BiFunction[Request, FeignMapping, Boolean]]) {
  val propertiesMerger = new IgnorablePropertiesMerger

  def findFeignMapping(request: Request) = {
    val feignMapping = feignProperties.mappings.find(mapping => {
      propertiesMerger.merge(mapping.request, feignProperties)

      matchers.forall(_.apply(request, mapping))
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
