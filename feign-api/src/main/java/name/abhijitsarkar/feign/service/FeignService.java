package name.abhijitsarkar.feign.service;

import name.abhijitsarkar.feign.model.Request;
import name.abhijitsarkar.feign.model.Response;
import reactor.core.publisher.Mono;

/**
 * @author Abhijit Sarkar
 */
public interface FeignService {
    Mono<Response> findFeignMapping(Request request);
}
