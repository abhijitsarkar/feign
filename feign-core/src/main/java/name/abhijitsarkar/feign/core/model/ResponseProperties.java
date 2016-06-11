package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class ResponseProperties {
    private Integer status;
    private Map<String, String> headers;
    private Body body;

    public ResponseProperties() {
        setStatus(status);
        setHeaders(headers);
        setBody(body);
    }

    public void setStatus(Integer status) {
        this.status = (status == null) ? OK.value() : status;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = (headers == null) ? emptyMap() : headers;
    }

    public void setBody(Body body) {
        this.body = (body == null) ? new Body() : body;
    }
}
