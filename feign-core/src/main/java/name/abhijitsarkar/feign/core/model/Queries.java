package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * @author Abhijit Sarkar
 */
public class Queries extends IgnorableRequestProperties {
    @Getter
    private Map<String, List<String>> pairs;

    public Queries() {
        setPairs(pairs);
    }

    public void setPairs(Map<String, List<String>> pairs) {
        this.pairs = (pairs == null) ? emptyMap() : pairs;
    }
}
