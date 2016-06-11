package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import name.abhijitsarkar.feign.core.matcher.HeadersMatcher;

import java.util.Map;
import java.util.function.Predicate;

import static java.util.Collections.emptyMap;

/**
 * @author Abhijit Sarkar
 */

public class Headers extends AbstractIgnorableRequestPart {
    @Getter
    private Map<String, String> pairs;
    @Getter
    private Class<? extends Predicate<RequestProperties>> matcher;

    public Headers() {
        setPairs(pairs);
        setMatcher(matcher);
    }

    public void setPairs(Map<String, String> pairs) {
        this.pairs = (pairs == null) ? emptyMap() : pairs;
    }

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? HeadersMatcher.class : matcher;
    }
}
