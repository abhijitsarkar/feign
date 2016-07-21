package name.abhijitsarkar.feign.core.model

import java.lang.{Boolean => JavaBoolean}

import name.abhijitsarkar.feign.core.model.Path.WILDCARD_PATTERN
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class PathSpec extends FlatSpec with Matchers {
  "path" should "set null uri to wildcard" in {
    val path = new Path
    path.setUri(null)
    path.uri shouldBe (WILDCARD_PATTERN)
  }

  it should "set uri" in {
    val uri = Table(null, WILDCARD_PATTERN, "/a")
    val path = new Path

    forAll(uri) { u =>
      path.setUri(u)
      path.uri shouldBe (u)
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
