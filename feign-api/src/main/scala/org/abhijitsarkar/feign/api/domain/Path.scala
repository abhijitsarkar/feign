package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */
case class Path(ignorableRequestProperties: Option[IgnorableRequestProperties] = None,
                uri: Option[String] = None)

