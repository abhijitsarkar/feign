package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

import static java.lang.Boolean.FALSE;

/**
 * @author Abhijit Sarkar
 */

public class Path {
    public static final String WILDCARD_PATTERN = "/**";

    @Getter
    private String uri;
    private Boolean ignoreCase;

    public Path() {
        setUri(uri);
        setIgnoreCase(ignoreCase);
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setUri(String uri) {
        this.uri = (uri == null) ? WILDCARD_PATTERN : uri;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }
}
