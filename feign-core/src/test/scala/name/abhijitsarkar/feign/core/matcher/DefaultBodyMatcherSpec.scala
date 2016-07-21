package name.abhijitsarkar.feign.core.matcher

import java.util.{List => JavaList, Map => JavaMap}

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class DefaultBodyMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val bodyMatcher = new DefaultBodyMatcher
  var feignProperties: FeignProperties = _
  var body: Body = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    body = new Body
    body.ignoreCase = feignProperties.ignoreCase
    body.ignoreUnknown = feignProperties.ignoreUnknown
    body.ignoreEmpty = feignProperties.ignoreEmpty

    requestProperties.body = body
  }

  "body matcher" should "match when no request body and no properties body" in {
    val request = new Request

    bodyMatcher(request, feignMapping) shouldBe true
  }

  it should "honor ignore unknown" in {
    val ignoreUnknown = Table("ignoreUnknown", true, false)
    val request = new Request
    request.setBody(Some("body"))
    body.raw = ""

    forAll(ignoreUnknown) { x =>
      body.ignoreUnknown = Option(x)

      bodyMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore empty" in {
    val ignoreEmpty = Table("ignoreEmpty", true, false)
    val request = new Request
    request.setBody(None)
    body.raw = ""

    forAll(ignoreEmpty) { x =>
      body.ignoreEmpty = Option(x)

      bodyMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "honor ignore case" in {
    val ignoreCase = Table("ignoreCase", true, false)
    val request = new Request
    request.setBody(Some("BODY"))
    body.raw = "body"

    forAll(ignoreCase) { x =>
      body.ignoreCase = Option(x)

      bodyMatcher(request, feignMapping) shouldBe x
    }
  }

  it should "match exact content" in {
    val request = new Request
    request.setBody(Some("body"))
    body.raw = "body"

    bodyMatcher(request, feignMapping) shouldBe true
  }

  it should "match regex content" in {
    val request = new Request
    request.setBody(Some("body"))
    body.raw = "b.*"

    bodyMatcher(request, feignMapping) shouldBe true
  }
}
