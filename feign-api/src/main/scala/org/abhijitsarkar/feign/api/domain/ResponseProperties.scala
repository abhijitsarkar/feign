package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */
case class ResponseProperties(status: Int = 200,
                              headers: Option[Headers] = None,
                              body: Option[Body] = None
                             )


