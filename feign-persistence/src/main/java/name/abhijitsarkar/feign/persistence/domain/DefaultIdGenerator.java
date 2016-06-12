package name.abhijitsarkar.feign.persistence.domain;


import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.IdGenerator;
import name.abhijitsarkar.feign.Request;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultIdGenerator implements IdGenerator {
    @Override
    public String id(Request request) {
        Map<String, String> headers = request.getHeaders();
        String requestId = null;

        if (isEmpty(headers) || StringUtils.isEmpty(headers.get("x-request-id"))) {
            requestId = UUID.randomUUID().toString();
            log.info("Generated random id: {}.", requestId);
        } else {
            requestId = headers.get("x-request-id");
            log.info("Using id from request: {}.", requestId);
        }

        return requestId;
    }
}
