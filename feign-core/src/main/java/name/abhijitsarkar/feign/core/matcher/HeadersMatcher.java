package name.abhijitsarkar.feign.core.matcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.core.model.Headers;
import name.abhijitsarkar.feign.core.model.RequestProperties;

import java.util.Map;
import java.util.function.Predicate;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
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
        boolean ignoreCase = headers.isIgnoreCase();

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

                    log.info("Comparing request header {}:[{}] with {}:[{}].",
                            key, valueFromRequest, key, pairs.get(key));

                    String valueFromRequestProperty = nullToEmpty(pairs.get(key));

                    if (ignoreCase) {
                        valueFromRequest = isNullOrEmpty(valueFromRequest) ?
                                valueFromRequest : valueFromRequest.toLowerCase();
                        valueFromRequestProperty = valueFromRequestProperty.toLowerCase();
                    }

                    boolean m = (valueFromRequest == null && valueFromRequestProperty.isEmpty())
                            || (valueFromRequest != null && valueFromRequestProperty.isEmpty() && ignoreUnknown)
                            || (valueFromRequest != null && valueFromRequest.matches(valueFromRequestProperty));

                    return m;
                });

        log.info("Headers match returned {}.", match);

        return match;
    }
}
