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
