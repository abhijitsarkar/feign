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

import com.jayway.jsonpath.JsonPath
import name.abhijitsarkar.feign.core.web.FeignController
import org.scalatest.{FlatSpec, Matchers}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.boot.test.{SpringApplicationConfiguration, TestRestTemplate, WebIntegrationTest}
import org.springframework.http.HttpMethod._
import org.springframework.test.context.{ActiveProfiles, TestContextManager}
import org.springframework.web.util.UriComponentsBuilder

/**
  * @author Abhijit Sarkar
  */
@SpringApplicationConfiguration(Array(classOf[FeignApp], classOf[FeignConfiguration]))
@WebIntegrationTest(randomPort = true)
@ActiveProfiles(Array("p2"))
class FeignSpecP2 extends FlatSpec with Matchers {
  @Autowired
  var FeignController: FeignController = _

  @Value("${local.server.port}")
  var port: Int = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  val restTemplate = new TestRestTemplate()
  val uriBuilder = UriComponentsBuilder.fromUriString(s"http://localhost:$port")

  "feign" should "record request using global id generator" in {
    val uri = uriBuilder.path("feign/abc").build().toUri()
    var response = restTemplate.exchange(uri, GET, null, classOf[String])
    response = restTemplate.exchange(s"http://localhost:$port/requests/feign-1357715445",
      GET, null, classOf[String])

    println(response)

    JsonPath.read[String](response.getBody, "$.path") shouldBe "/feign/abc"
  }

  it should "record request using given id generator" in {
    val uri = uriBuilder.path("feign/xyz").build().toUri()
    var response = restTemplate.exchange(uri, GET, null, classOf[String])
    response = restTemplate.exchange(s"http://localhost:$port/requests/1",
      GET, null, classOf[String])

    println(response)

    JsonPath.read[String](response.getBody, "$.path") shouldBe "/feign/xyz"
  }
}
