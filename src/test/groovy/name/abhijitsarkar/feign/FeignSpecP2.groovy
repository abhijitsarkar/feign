package name.abhijitsarkar.feign

import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

import static org.springframework.http.HttpMethod.GET
/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles("p2")
class FeignSpecP2 extends AbstractFeignSpec {
    def "records request using ConstantIdGenerator"() {
        given:
        def uri = uriBuilder.path('feign/xyz').build().toUri()

        when:
        HttpHeaders headers = new HttpHeaders()
        headers.add('x-request-id', '101')

        HttpEntity<Void> entity = new HttpEntity<Void>(null, headers)

        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, entity, String)

        and:
        response = restTemplate.exchange(new URI("http://localhost:$port/requests/1"), GET, null, String)

        println(response)

        then:
        JsonPath.read(response.body, '$.path') == '/feign/xyz'
    }
}