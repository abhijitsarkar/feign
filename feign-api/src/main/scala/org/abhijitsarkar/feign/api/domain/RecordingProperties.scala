package org.abhijitsarkar.feign.api.domain

import org.abhijitsarkar.feign.api.persistence.{IdGenerator, RequestService}

/**
  * @author Abhijit Sarkar
  */
case class RecordingProperties(disable: Option[Boolean] = None,
                               idGenerator: Option[Class[_ <: IdGenerator]] = None,
                               recordingService: Option[Class[_ <: RequestService]] = None
                              )



