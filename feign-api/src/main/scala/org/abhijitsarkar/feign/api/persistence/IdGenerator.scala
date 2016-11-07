package org.abhijitsarkar.feign.api.persistence

import org.abhijitsarkar.feign.api.model.Request

/**
  * @author Abhijit Sarkar
  */
trait IdGenerator {
  def id(request: Request): String
}
