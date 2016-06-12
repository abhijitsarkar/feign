package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * @author Abhijit Sarkar
 */

public class Headers extends IgnorableRequestProperties {
    @Getter
    private Map<String, String> pairs;

    public Headers() {
        setPairs(pairs);
    }

    public void setPairs(Map<String, String> pairs) {
        this.pairs = (pairs == null) ? emptyMap() : pairs;
    }
}
