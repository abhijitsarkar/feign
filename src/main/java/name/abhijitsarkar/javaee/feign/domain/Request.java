package name.abhijitsarkar.javaee.feign.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * @author Abhijit Sarkar
 */
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Request {
    @Id
    private String id;
    private String path;
    private String method;
    private Map<String, String[]> queryParams;
    private Map<String, String> headers;
    private String body;
}
