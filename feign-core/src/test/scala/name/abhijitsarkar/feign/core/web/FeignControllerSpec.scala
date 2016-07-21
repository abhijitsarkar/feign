package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.core.model.{Body, FeignMapping, ResponseProperties}
import name.abhijitsarkar.feign.core.service.FeignService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.springframework.http.HttpStatus

/**
  * @author Abhijit Sarkar
  */
class FeignControllerSpec extends FlatSpec with Matchers with BeforeAndAfter {
  var feignController: FeignController = _
  var feignService: FeignService = _
  var feignMapping: FeignMapping = _
  var responseProperties: ResponseProperties = _

  before {
    feignService = mock(classOf[FeignService])
    feignController = new FeignController(feignService)

    feignMapping = new FeignMapping
    responseProperties = new ResponseProperties
    feignMapping.response = responseProperties
  }

  "feign controller" should "return 404 response if no mapping found" in {
    when(feignService.findFeignMapping(any())).thenReturn(None, Nil: _*)

    val retVal = feignController.all(null)

    retVal.getStatusCode shouldEqual (HttpStatus.NOT_FOUND)
  }

  it should "return response with status specified by mapping" in {
    responseProperties.status = HttpStatus.OK.value()
    when(feignService.findFeignMapping(any())).thenReturn(Some(feignMapping), Nil: _*)

    val retVal = feignController.all(null)

    retVal.getStatusCode.value() shouldEqual (responseProperties.status)
  }

  it should "return headers if specified by mapping" in {
    responseProperties.headers = Map("a" -> "b")
    when(feignService.findFeignMapping(any())).thenReturn(Some(feignMapping), Nil: _*)

    val retVal = feignController.all(null)

    retVal.getHeaders.toSingleValueMap should contain value ("b")
  }

  it should "return body if specified by mapping" in {
    val body = new Body
    body.setRaw("body")
    responseProperties.body = body
    when(feignService.findFeignMapping(any())).thenReturn(Some(feignMapping), Nil: _*)

    val retVal = feignController.all(null)

    retVal.getBody shouldEqual ("body")
  }
}
