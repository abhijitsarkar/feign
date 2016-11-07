package org.abhijitsarkar.feign.api.model

import scala.collection.immutable.{Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
case class Request(path: String,
                   method: String,
                   queries: ImmutableMap[String, Seq[String]] = ImmutableMap(),
                   headers: ImmutableMap[String, String] = ImmutableMap(),
                   body: Option[String] = None)

