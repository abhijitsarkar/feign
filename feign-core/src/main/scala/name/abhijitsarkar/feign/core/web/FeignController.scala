package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.Request
import name.abhijitsarkar.feign.core.service.FeignService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.{HttpHeaders, HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.RequestMapping

import scala.collection.JavaConverters._

/**
  * @author Abhijit Sarkar
  */
class FeignController @Autowired()(val feignService: FeignService) {
  @RequestMapping(path = Array("/feign/**"), produces = Array(APPLICATION_JSON_VALUE))
  def all(request: Request) = {
    val feignMapping = feignService.findFeignMapping(request)

    feignMapping.map(_.response).map { response =>
      val httpHeaders = new HttpHeaders

      response.headers.foreach { (header) => httpHeaders.put(header._1, List(header._2).asJava) }

      response.body.getContent match {
        case Some(content) => new ResponseEntity[String](content, httpHeaders, HttpStatus.valueOf(response.status))
        case None => new ResponseEntity[Void](httpHeaders, HttpStatus.valueOf(response.status))
      }
    }.getOrElse(new ResponseEntity[Void](NOT_FOUND))
  }
}
