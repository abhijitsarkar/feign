package name.abhijitsarkar.javaee.feign.model;

import lombok.Getter;
import name.abhijitsarkar.javaee.feign.matcher.HeadersMatcher;

import java.util.Map;
import java.util.function.Predicate;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyMap;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class Headers {
    private boolean ignoreUnknown;
    private boolean ignoreEmpty;
    private Map<String, String> pairs;
    private Class<? extends Predicate<RequestProperties>> matcher;

    public Headers() {
        setIgnoreUnknown(ignoreUnknown);
        setIgnoreEmpty(ignoreEmpty);
        setPairs(pairs);
        setMatcher(matcher);
    }

    public void setIgnoreUnknown(Boolean ignoreUnknown) {
        this.ignoreUnknown = (ignoreUnknown == null) ? TRUE : ignoreUnknown;
    }

    public void setIgnoreEmpty(Boolean ignoreEmpty) {
        this.ignoreEmpty = (ignoreEmpty == null) ? TRUE : ignoreEmpty;
    }

    public void setPairs(Map<String, String> pairs) {
        this.pairs = (pairs == null) ? emptyMap() : pairs;
    }

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? HeadersMatcher.class : matcher;
    }
}
