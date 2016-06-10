package name.abhijitsarkar.javaee.feign.model;

import lombok.Getter;
import name.abhijitsarkar.javaee.feign.matcher.PathMatcher;

import java.util.function.Predicate;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class Path {
    String uri;
    private Class<? extends Predicate<RequestProperties>> matcher;

    public Path() {
        setUri(uri);
        setMatcher(matcher);
    }

    public void setUri(String uri) {
        this.uri = (uri == null) ? "/**" : uri;
    }

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? PathMatcher.class : matcher;
    }
}
