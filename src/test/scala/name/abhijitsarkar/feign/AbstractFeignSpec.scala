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

package name.abhijitsarkar.feign

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.{SpringApplicationConfiguration, TestRestTemplate, WebIntegrationTest}
import org.springframework.web.util.UriComponentsBuilder

/**
  * @author Abhijit Sarkar
  */
//@SpringApplicationConfiguration(Array(classOf[FeignApp], classOf[FeignConfiguration]))
//@WebIntegrationTest(randomPort = true)
//abstract class AbstractFeignSpec {
//  @Value("${local.server.port}")
//  protected var port: Int
//
//  protected val restTemplate = new TestRestTemplate()
//
//  protected var uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:$port")
//}
