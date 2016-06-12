package name.abhijitsarkar.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author Abhijit Sarkar
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Request {
    protected String path;
    protected String method;
    protected Map<String, String[]> queryParams;
    protected Map<String, String> headers;
    protected String body;
}
