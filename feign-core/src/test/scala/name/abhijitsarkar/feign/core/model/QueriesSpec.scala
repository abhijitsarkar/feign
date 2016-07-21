package name.abhijitsarkar.feign.core.model

import java.util.{List => JavaList, Map => JavaMap}

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class QueriesSpec extends FlatSpec with Matchers {
  "queries" should "set null pairs to empty" in {
    val queries = new Queries
    queries.setPairs(null)

    queries.pairs shouldBe empty
  }

  it should "set pairs" in {
    val queries = new Queries
    val m = mapAsJavaMap(Map("a" -> List("b").asJava))
    queries.setPairs(m)

    queries.pairs("a") should contain("b")
  }
}
