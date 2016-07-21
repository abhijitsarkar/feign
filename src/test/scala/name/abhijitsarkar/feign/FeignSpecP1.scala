package name.abhijitsarkar.feign

import java.net.URI

import name.abhijitsarkar.feign.core.web.FeignController
import org.scalatest.{FlatSpec, Matchers}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.boot.test.{SpringApplicationConfiguration, TestRestTemplate, WebIntegrationTest}
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.client.Traverson
import org.springframework.hateoas.{MediaTypes, Resource}
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.{ActiveProfiles, TestContextManager}
import org.springframework.web.util.UriComponentsBuilder

/**
  * @author Abhijit Sarkar
  */
@SpringApplicationConfiguration(Array(classOf[FeignApp], classOf[FeignConfiguration]))
@WebIntegrationTest(randomPort = true)
@ActiveProfiles(Array("p1"))
class FeignSpecP1 extends FlatSpec with Matchers {
  @Autowired
  var FeignController: FeignController = _
  @Value("${local.server.port}")
  var port: Int = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  val restTemplate = new TestRestTemplate()
  val uriBuilder = UriComponentsBuilder.fromUriString(s"http://localhost:$port")

  "feign" should "match POST request and find it" in {
    val uri = uriBuilder.path("feign/abc").build().toUri()
    val response = restTemplate.exchange(uri, POST, null, classOf[String])

    response.getStatusCode shouldBe OK

    val traverson = new Traverson(new URI("http://localhost:$port"), MediaTypes.HAL_JSON)

    val t = new ParameterizedTypeReference[Resource[Request]]() {}

    def request = traverson.
      follow("requests").
      follow("$._embedded.requests[0]._links.self.href").
      toObject(t)
      .getContent

    println(request)

    request.path shouldEqual "/feign/abc"
    request.method shouldEqual "POST"
  }
}
