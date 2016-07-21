package name.abhijitsarkar.feign.persistence

import name.abhijitsarkar.feign.Request

/**
  * @author Abhijit Sarkar
  */
class ConstantIdGenerator extends IdGenerator {
  override def id(request: Request) = "1"
}
