package name.abhijitsarkar.feign.core.model

import java.util.{Map => JavaMap}

import org.springframework.http.HttpStatus.OK

import scala.collection.JavaConverters._
import scala.collection.immutable.{Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class ResponseProperties {
  var status: Integer = _
  var headers: ImmutableMap[String, String] = _
  var body: Body = _

  setStatus(null)
  setHeaders(null)
  setBody(null)

  def getStatus = status

  def setStatus(status: Integer) {
    this.status = if (status == null) Integer.valueOf(OK.value)
    else status
  }

  def getHeaders = headers

  def setHeaders(headers: JavaMap[String, String]) {
    this.headers = if (headers == null) ImmutableMap()
    else headers.asScala.toMap
  }

  def getBody = body

  def setBody(body: Body) {
    this.body = if (body == null) new Body
    else body
  }

  override def toString = s"ResponseProperties($status, $headers, $body)"
}
