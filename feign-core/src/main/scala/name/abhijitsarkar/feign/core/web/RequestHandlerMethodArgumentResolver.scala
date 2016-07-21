package name.abhijitsarkar.feign.core.web

import javax.servlet.http.HttpServletRequest

import name.abhijitsarkar.feign.Request
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.{HandlerMethodArgumentResolver, ModelAndViewContainer}

import scala.collection.JavaConverters._
import scala.collection.immutable.{Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class RequestHandlerMethodArgumentResolver extends HandlerMethodArgumentResolver {
  override def resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer,
                               webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory): Request = {
    val request = webRequest.getNativeRequest.asInstanceOf[HttpServletRequest]

    val headers = request.getHeaderNames.asScala.foldLeft(ImmutableMap[String, String]()) { (map, h) =>
      map + (h -> request.getHeader(h))
    }

    val br = request.getReader
    val body = Stream.continually(br.readLine()).takeWhile(_ != null).mkString("\n")

    val req = new Request()
    req.path = request.getServletPath
    req.method = request.getMethod
    req.queryParams = request.getParameterMap.asScala.mapValues(_.toList).toMap
    req.headers = headers
    req.body = Option(body)

    req
  }

  override def supportsParameter(parameter: MethodParameter): Boolean =
    classOf[Request].isAssignableFrom(parameter.getParameterType)
}
