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

import java.util.{Map => JavaMap}

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class HeadersSpec extends FlatSpec with Matchers {
  "headers" should "set null pairs to empty" in {
    val headers = new Headers
    headers.setPairs(null)

    headers.pairs shouldBe empty
  }

  it should "set pairs" in {
    val headers = new Headers
    val m = mapAsJavaMap(Map("a" -> "b"))
    headers.setPairs(m)

    headers.pairs.get("a") shouldEqual ("b")
  }
}
