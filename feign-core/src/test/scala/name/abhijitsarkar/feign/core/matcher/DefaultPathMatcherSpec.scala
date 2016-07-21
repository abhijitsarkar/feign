package name.abhijitsarkar.feign.core.matcher

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.model.{FeignMapping, FeignProperties, Path, RequestProperties}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class DefaultPathMatcherSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val pathMatcher = new DefaultPathMatcher
  var feignProperties: FeignProperties = _
  var path: Path = _
  var feignMapping: FeignMapping = _
  var requestProperties: RequestProperties = _

  before {
    feignProperties = new FeignProperties
    feignProperties.postConstruct
    feignMapping = new FeignMapping
    requestProperties = new RequestProperties
    feignMapping.request = requestProperties

    path = new Path
    path.ignoreCase = feignProperties.ignoreCase

    requestProperties.path = path
  }

  "path matcher" should "match exact path" in {
    path.uri = "/abc/xyz"
    val request = new Request
    request.path = "/abc/xyz"

    pathMatcher(request, feignMapping) shouldBe true
  }

  it should "match regex path" in {
    path.uri = "/abc/*"
    val request = new Request
    request.path = "/abc/xyz"

    pathMatcher(request, feignMapping) shouldBe true
  }

  it should "match using ignore case" in {
    path.uri = "/abc/xyz"
    path.ignoreCase = Some(true)
    val request = new Request
    request.path = "/abc/XYZ"

    pathMatcher(request, feignMapping) shouldBe true
  }

  it should "not match" in {
    path.uri = "/abc"
    val request = new Request
    request.path = "/xyz"

    pathMatcher(request, feignMapping) shouldBe false
  }
}
