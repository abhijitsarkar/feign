package name.abhijitsarkar.feign.persistence.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import name.abhijitsarkar.feign.Request;
import org.springframework.data.annotation.Id;

/**
 * @author Abhijit Sarkar
 */
@Getter
@NoArgsConstructor
@ToString
public class MongoDbRecordingRequest extends Request {
    @Id
    private String id;

    public MongoDbRecordingRequest(Request request, String id) {
        path = request.getPath();
        method = request.getMethod();
        queryParams = request.getQueryParams();
        headers = request.getHeaders();
        body = request.getBody();

        this.id = id;
    }
}
