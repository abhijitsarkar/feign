package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class RequestProperties {
    private Path path;
    private Method method;
    private Queries queries;
    private Headers headers;
    private Body body;

    public RequestProperties() {
        setPath(path);
        setMethod(method);
        setQueries(queries);
        setHeaders(headers);
        setBody(body);
    }

    public void setPath(Path path) {
        this.path = (path == null) ? new Path() : path;
    }

    public void setMethod(Method method) {
        this.method = (method == null) ? new Method() : method;
    }

    public void setQueries(Queries queries) {
        this.queries = (queries == null) ? new Queries() : queries;
    }

    public void setHeaders(Headers headers) {
        this.headers = (headers == null) ? new Headers() : headers;
    }

    public void setBody(Body body) {
        this.body = (body == null) ? new Body() : body;
    }
}
