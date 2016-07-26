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

import java.lang.{Boolean => JavaBoolean}

import name.abhijitsarkar.feign.core.model.Method.WILDCARD
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.Tables.Table
import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class MethodSpec extends FlatSpec with Matchers {
  "method" should "set null method to wildcard" in {
    val method = new Method
    method.setName(null)

    method.name shouldBe (WILDCARD)
  }

  it should "set method" in {
    val methods = Table(WILDCARD, "GET")
    val method = new Method

    forAll(methods) { m =>
      method.setName(m)
      method.name shouldBe (m)
    }
  }

  "path" should "set null ignoreCase to false" in {
    val path = new Path

    path.setIgnoreCase(null.asInstanceOf[JavaBoolean])
    path.ignoreCase shouldBe (false)
  }

  it should "set ignoreCase" in {
    val ignoreCase = List(JavaBoolean.TRUE, JavaBoolean.FALSE)
    val path = new Path

    ignoreCase.foreach { b =>
      path.setIgnoreCase(b)
      path.ignoreCase shouldBe (b)
    }
  }
}
