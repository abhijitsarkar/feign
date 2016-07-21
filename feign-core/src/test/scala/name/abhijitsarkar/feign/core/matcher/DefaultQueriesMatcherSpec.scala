package name.abhijitsarkar.feign.core.matcher

import java.util.{List => JavaList, Map => JavaMap}

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class DefaultQueriesMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val queriesMatcher = new DefaultQueriesMatcher
  var feignProperties: FeignProperties = _
  var queries: Queries = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    queries = new Queries()
    queries.ignoreCase = feignProperties.ignoreCase
    queries.ignoreUnknown = feignProperties.ignoreUnknown
    queries.ignoreEmpty = feignProperties.ignoreEmpty

    requestProperties.queries = queries
  }

  "queries matcher" should "match when no request params and no properties params" in {
    val request = new Request

    queriesMatcher(request, feignMapping) shouldBe true
  }

  it should "honor ignore unknown" in {
    val ignoreUnknown = Table("ignoreUnknown", true, false)
    val request = new Request
    request.setQueryParams(Map("a" -> List("x", "y")))

    forAll(ignoreUnknown) { x =>
      queries.ignoreUnknown = Option(x)

      queriesMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore empty" in {
    val ignoreEmpty = Table("ignoreEmpty", true, false)
    val request = new Request
    queries.setPairs(Map("a" -> List("x", "y")).mapValues(_.asJava).asJava)

    forAll(ignoreEmpty) { x =>
      queries.ignoreEmpty = Option(x)

      queriesMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore case" in {
    val ignoreCase = Table("ignoreCase", true, false)
    val request = new Request
    request.setQueryParams(Map("a" -> List("X", "Y")))
    queries.setPairs(Map("a" -> List("x", "y")).mapValues(_.asJava).asJava)
    queries.ignoreUnknown = Some(false)

    forAll(ignoreCase) { x =>
      queries.ignoreCase = Option(x)

      queriesMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "match even if values are unsorted" in {
    val request = new Request
    request.setQueryParams(Map("a" -> List("x", "y")))
    queries.setPairs(Map("a" -> List("y", "x")).mapValues(_.asJava).asJava)

    queriesMatcher(request, feignMapping) shouldBe true
  }

  it should "match regex param values" in {
    val request = new Request
    request.setQueryParams(Map("a" -> List("x", "y.*")))
    queries.setPairs(Map("a" -> List("x", "yz")).mapValues(_.asJava).asJava)

    queriesMatcher(request, feignMapping) shouldBe true
  }
}
