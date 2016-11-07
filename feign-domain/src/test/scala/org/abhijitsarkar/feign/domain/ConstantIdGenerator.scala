package org.abhijitsarkar.feign.domain

import org.abhijitsarkar.feign.api.model.Request
import org.abhijitsarkar.feign.api.persistence.IdGenerator

/**
  * @author Abhijit Sarkar
  */
class ConstantIdGenerator extends IdGenerator {
  override def id(request: Request): String = "id"
}
