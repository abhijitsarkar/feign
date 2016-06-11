package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import name.abhijitsarkar.feign.core.matcher.MethodMatcher;

import java.util.function.Predicate;

import static java.lang.Boolean.FALSE;

/**
 * @author Abhijit Sarkar
 */
public class Method {
    public static final String WILDCARD = ".*";

    @Getter
    private String name;
    @Getter
    private Class<? extends Predicate<RequestProperties>> matcher;

    private Boolean ignoreCase;

    public Method() {
        setName(name);
        setMatcher(matcher);
        setIgnoreCase(ignoreCase);
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setName(String name) {
        this.name = (name == null) ? WILDCARD : name;
    }

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? MethodMatcher.class : matcher;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }
}
