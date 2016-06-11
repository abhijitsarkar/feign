package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import name.abhijitsarkar.feign.core.matcher.QueriesMatcher;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Collections.emptyMap;

/**
 * @author Abhijit Sarkar
 */
public class Queries extends AbstractIgnorableRequestPart {
    @Getter
    private Map<String, List<String>> pairs;
    @Getter
    private Class<? extends Predicate<RequestProperties>> matcher;

    public Queries() {
        setPairs(pairs);
        setMatcher(matcher);
    }

    public void setPairs(Map<String, List<String>> pairs) {
        this.pairs = (pairs == null) ? emptyMap() : pairs;
    }

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? QueriesMatcher.class : matcher;
    }
}
