package name.abhijitsarkar.javaee.feign

import name.abhijitsarkar.javaee.feign.domain.Request
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resources
import org.springframework.hateoas.client.Traverson
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK
/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles("p1")
class FeignSpec extends AbstractFeignSpec {
    def "exactly matches request path"() {
        given:
        def uri = uriBuilder.path('feign/abc').build().toUri()

        when:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, null, String)

        then:
        response.statusCode == OK
    }

    def "records request and can find it"() {
        given:
        def uri = uriBuilder.path('feign/xyz').build().toUri()

        when:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, null, String)
        assert response.statusCode == OK

        and:
        Traverson traverson = new Traverson(new URI("http://localhost:$port"), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<Resources<Request>> type =
                new ParameterizedTypeReference<Resources<Request>>() {};

        def allRequests = traverson
                .follow('requests')
                .toObject(type)
        println(allRequests)

        then:
        allRequests.first().path == '/feign/xyz'
    }
}