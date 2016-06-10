package name.abhijitsarkar.javaee.feign.web;

import name.abhijitsarkar.javaee.feign.domain.Request;
import name.abhijitsarkar.javaee.feign.model.Body;
import name.abhijitsarkar.javaee.feign.model.FeignMapping;
import name.abhijitsarkar.javaee.feign.model.ResponseProperties;
import name.abhijitsarkar.javaee.feign.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@RestController
public class FeignController {
    @Autowired
    private FeignService feignService;

    @RequestMapping(path = "/feign/**", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> all(Request request) {
        Optional<FeignMapping> feignMapping = feignService.findFeignMapping(request);

        if (feignMapping.isPresent()) {
            ResponseProperties rp = feignMapping.get().getResponse();

            HttpHeaders httpHeaders = new HttpHeaders();

            if (!isEmpty(rp.getHeaders())) {
                rp.getHeaders().entrySet()
                        .forEach(e -> httpHeaders.put(e.getKey(), singletonList(e.getValue())));
            }

            Body responseBody = rp.getBody();

            if (!StringUtils.isEmpty(responseBody.toString())) {
                return new ResponseEntity<String>(responseBody.toString(), httpHeaders, HttpStatus.valueOf(rp.getStatus()));
            }

            return new ResponseEntity<Void>(httpHeaders, HttpStatus.valueOf(rp.getStatus()));
        }

        return new ResponseEntity<Void>(NOT_FOUND);
    }
}
