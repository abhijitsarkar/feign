package name.abhijitsarkar.feign

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

import static org.springframework.http.HttpMethod.GET

/**
 * @author Abhijit Sarkar
 */
@ActiveProfiles("p3")
class FeignSpecP3 extends AbstractFeignSpec {
    def "matches using AlwaysTrueMatcher"() {
        given:
        def uri = uriBuilder.path('feign/xyz').build().toUri()

        when:
        def ResponseEntity<String> response =
                restTemplate.exchange(uri, GET, null, String)

        println(response)

        then:
        response.statusCode == HttpStatus.OK
    }
}