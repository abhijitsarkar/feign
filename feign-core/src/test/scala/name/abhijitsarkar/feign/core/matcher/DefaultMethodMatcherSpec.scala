package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class DefaultMethodMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val methodMatcher = new DefaultMethodMatcher
  var feignProperties: FeignProperties = _
  var method: Method = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    method = new Method
    method.ignoreCase = feignProperties.ignoreCase

    requestProperties.method = method
  }

  "method matcher" should "match exact method" in {
    method.name = "GET"
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe true
  }

  import Method.WILDCARD

  it should "match regex method" in {
    method.name = WILDCARD
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe true
  }

  it should "match using ignore case" in {
    method.name = "get"
    method.ignoreCase = Some(true)
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe true
  }

  it should "not match" in {
    method.name = "POST"
    val request = new Request
    request.method = "GET"

    methodMatcher(request, feignMapping) shouldBe false
  }
}
