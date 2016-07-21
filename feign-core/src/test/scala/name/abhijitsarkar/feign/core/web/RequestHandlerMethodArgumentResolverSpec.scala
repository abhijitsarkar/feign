package name.abhijitsarkar.feign.core.web

import java.io.{BufferedReader, StringReader}
import javax.servlet.http.HttpServletRequest

import name.abhijitsarkar.feign.Request
import org.mockito.Mockito._
import org.scalatest.{FlatSpec, Matchers}
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class RequestHandlerMethodArgumentResolverSpec extends FlatSpec with Matchers {
  "request handler method argument resolver" should "build request from HttpServletRequest" in {
    val httpServletRequest = mock(classOf[HttpServletRequest])
    when(httpServletRequest.getHeaderNames).thenReturn(List("a").toIterator.asJavaEnumeration, Nil: _*)
    when(httpServletRequest.getHeader("a")).thenReturn("b", Nil: _*)
    when(httpServletRequest.getReader).thenReturn(new BufferedReader(new StringReader("body")), Nil: _*)
    when(httpServletRequest.getServletPath).thenReturn("/abc", Nil: _*)
    when(httpServletRequest.getMethod).thenReturn("GET", Nil: _*)
    when(httpServletRequest.getParameterMap).thenReturn(Map("a" -> Array("b")).asJava, Nil: _*)

    val webRequest = mock(classOf[NativeWebRequest])
    when(webRequest.getNativeRequest).thenReturn(httpServletRequest, Nil: _*)

    val request = new RequestHandlerMethodArgumentResolver().resolveArgument(
      null, null, webRequest, null)

    request.path shouldEqual httpServletRequest.getServletPath
    request.method shouldEqual httpServletRequest.getMethod
    request.queryParams should contain value (List("b"))
    request.headers shouldEqual Map("a" -> "b")
    request.body shouldEqual Some("body")
  }

//  it should "support Request class" in {
//    val methodParam = mock(classOf[MethodParameter])
//    // Cannot get this shit to compile
//    when(methodParam.getParameterType).thenReturn(classOf[Request], classOf[Request])
//
//    new RequestHandlerMethodArgumentResolver().supportsParameter(methodParam) shouldBe true
//  }
}
