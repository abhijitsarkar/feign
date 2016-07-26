/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 */

package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.service.FeignService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.{HttpHeaders, HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
@RestController
class FeignController @Autowired()(val feignService: FeignService) {
  @RequestMapping(path = Array("/feign/**"), produces = Array(APPLICATION_JSON_VALUE))
  def all(request: Request) = {
    val feignMapping = feignService.findFeignMapping(request)

    feignMapping.map(_.response).map { response =>
      val httpHeaders = new HttpHeaders

      response.headersToScala.foreach { (header) => httpHeaders.put(header._1, List(header._2).asJava) }

      response.body.getContent match {
        case Some(content) => new ResponseEntity[String](content, httpHeaders, HttpStatus.valueOf(response.status))
        case None => new ResponseEntity[Void](httpHeaders, HttpStatus.valueOf(response.status))
      }
    }.getOrElse(new ResponseEntity[Void](NOT_FOUND))
  }
}
