package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.domain.FeignMapping._

case class FeignMapping(request: RequestProperties = defaultRequestProperties,
                        responses: Seq[ResponseProperties] = defaultResponseProerties)

object FeignMapping {
  val defaultRequestProperties = new RequestProperties
  val defaultResponseProerties = Seq[ResponseProperties]()
}
