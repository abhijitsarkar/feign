package org.abhijitsarkar.feign.api.domain

/**
  * @author Abhijit Sarkar
  */

import org.abhijitsarkar.feign.api.domain.RequestProperties._

case class RequestProperties(path: Path = defaultPath,
                             method: Method = defaultMethod,
                             queries: Queries = defaultQueries,
                             headers: Headers = defaultHeaders,
                             body: Body = defaultBody
                            )

object RequestProperties {
  val defaultPath = new Path
  val defaultMethod = new Method
  val defaultQueries = new Queries
  val defaultHeaders = new Headers
  val defaultBody = new Body
}

