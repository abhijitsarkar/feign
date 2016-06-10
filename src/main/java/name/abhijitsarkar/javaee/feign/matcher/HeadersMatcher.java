package name.abhijitsarkar.javaee.feign.matcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.javaee.feign.model.Headers;
import name.abhijitsarkar.javaee.feign.model.RequestProperties;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@RequiredArgsConstructor
@Slf4j
public class HeadersMatcher implements Predicate<RequestProperties> {
    private final Map<String, String> pairs;

    @Override
    public boolean test(RequestProperties requestProperties) {
        Headers headers = requestProperties.getHeaders();

        Map<String, String> pairs = headers.getPairs();
        boolean ignoreUnknown = headers.isIgnoreUnknown();
        boolean ignoreEmpty = headers.isIgnoreEmpty();

        if (!isEmpty(this.pairs) && isEmpty(pairs) && !ignoreUnknown) {
            log.info("Request headers are not empty but request property headers are, " +
                    "and ignoreUnknown is false. Headers match returned false.");

            return false;
        }

        if (isEmpty(this.pairs)) {
            boolean match = ignoreEmpty || isEmpty(pairs);

            log.info("Request headers are empty. Headers match returned {}.", match);

            return match;
        }

        boolean match = this.pairs.entrySet().stream()
                .allMatch(e -> {
                    String key = e.getKey();
                    String valueFromRequest = e.getValue();
                    String valueFromRequestProperty = pairs.get(key);

                    boolean m = (valueFromRequest != null && valueFromRequestProperty == null && ignoreUnknown)
                            || Objects.equals(valueFromRequest, valueFromRequestProperty);

                    log.info("Comparing request header {}:[{}] with {}:[{}].",
                            key, valueFromRequest, key, valueFromRequestProperty);

                    return m;
                });

        log.info("Headers match returned {}.", match);

        return match;
    }
}
