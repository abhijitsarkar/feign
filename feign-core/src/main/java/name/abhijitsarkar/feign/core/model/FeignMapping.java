package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class FeignMapping extends IgnorableRequestProperties {
    private RequestProperties request;
    private ResponseProperties response;

    public FeignMapping() {
        setRequest(request);
        setResponse(response);
    }

    public void setRequest(RequestProperties requestProperties) {
        this.request = (requestProperties == null) ? new RequestProperties() : requestProperties;
    }

    public void setResponse(ResponseProperties responseProperties) {
        this.response = (responseProperties == null) ? new ResponseProperties() : responseProperties;
    }
}
