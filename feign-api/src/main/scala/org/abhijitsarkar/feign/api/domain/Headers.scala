package org.abhijitsarkar.feign.api.domain

import scala.collection.immutable.{Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
case class Headers(ignorableRequestProperties: Option[IgnorableRequestProperties] = None,
                   pairs: ImmutableMap[String, String] = ImmutableMap())


