package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import name.abhijitsarkar.feign.core.matcher.PathMatcher;

import java.util.function.Predicate;

import static java.lang.Boolean.FALSE;

/**
 * @author Abhijit Sarkar
 */

public class Path {
    public static final String WILDCARD_PATTERN = "/**";

    @Getter
    private String uri;
    @Getter
    private Class<? extends Predicate<RequestProperties>> matcher;

    private Boolean ignoreCase;

    public Path() {
        setUri(uri);
        setMatcher(matcher);
        setIgnoreCase(ignoreCase);
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setUri(String uri) {
        this.uri = (uri == null) ? WILDCARD_PATTERN : uri;
    }

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? PathMatcher.class : matcher;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }
}
