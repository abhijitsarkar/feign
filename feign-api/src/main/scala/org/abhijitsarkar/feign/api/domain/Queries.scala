package org.abhijitsarkar.feign.api.domain

import scala.collection.immutable.{Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
case class Queries(ignorableRequestProperties: Option[IgnorableRequestProperties] = None,
                   pairs: ImmutableMap[String, Seq[String]] = ImmutableMap())
