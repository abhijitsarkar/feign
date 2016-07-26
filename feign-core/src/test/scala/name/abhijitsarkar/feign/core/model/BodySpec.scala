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

package name.abhijitsarkar.feign.core.model

import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class BodySpec extends FlatSpec with Matchers {
  "body" should "get content when raw is set" in {
    val body = new Body
    body.setRaw("body")

    body.content shouldBe Some("body")
  }

  it should "get content when url is set" in {
    val body = new Body
    body.setUrl(getClass().getResource("/body.txt").toString)

    body.content shouldBe Some("body")
  }
}
