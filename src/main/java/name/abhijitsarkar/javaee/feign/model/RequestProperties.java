package name.abhijitsarkar.javaee.feign.model;

import lombok.Getter;

import static java.lang.Boolean.TRUE;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class RequestProperties {
    private Boolean record;
    private Class<? extends IdGenerator> idGenerator;
    private Path path;
    private Method method;
    private Queries queries;
    private Headers headers;
    private Body body;

    public RequestProperties() {
        setRecord(record);
        setIdGenerator(idGenerator);
        setPath(path);
        setMethod(method);
        setQueries(queries);
        setHeaders(headers);
        setBody(body);
    }

    public void setRecord(Boolean record) {
        this.record = (record == null) ? TRUE : record;
    }

    public void setIdGenerator(Class<? extends IdGenerator> idGenerator) {
        this.idGenerator = (idGenerator == null) ? DefaultIdGenerator.class : idGenerator;
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
