package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */
case class Method(ignorableRequestProperties: Option[IgnorableRequestProperties] = None,
                  name: Option[String] = None)



