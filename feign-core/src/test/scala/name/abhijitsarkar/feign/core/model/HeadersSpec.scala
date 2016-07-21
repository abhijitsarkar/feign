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

    headers.pairs("a") shouldBe ("b")
  }
}
