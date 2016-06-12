package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

import static java.lang.Boolean.FALSE;

/**
 * @author Abhijit Sarkar
 */
public class Method {
    public static final String WILDCARD = ".*";

    @Getter
    private String name;
    private Boolean ignoreCase;

    public Method() {
        setName(name);
        setIgnoreCase(ignoreCase);
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setName(String name) {
        this.name = (name == null) ? WILDCARD : name;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }
}
