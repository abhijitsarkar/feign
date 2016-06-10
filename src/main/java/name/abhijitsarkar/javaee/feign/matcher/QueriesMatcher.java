package name.abhijitsarkar.javaee.feign.matcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.javaee.feign.model.Queries;
import name.abhijitsarkar.javaee.feign.model.RequestProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@RequiredArgsConstructor
@Slf4j
public class QueriesMatcher implements Predicate<RequestProperties> {
    private final Map<String, String[]> pairs;

    @Override
    public boolean test(RequestProperties requestProperties) {
        Queries queries = requestProperties.getQueries();

        Map<String, List<String>> pairs = queries.getPairs();
        boolean ignoreUnknown = queries.isIgnoreUnknown();
        boolean ignoreEmpty = queries.isIgnoreEmpty();

        if (!isEmpty(this.pairs) && isEmpty(pairs) && !ignoreUnknown) {
            log.info("Query params are not empty but request property queries are, " +
                    "and ignoreUnknown is false. Queries match returned false.");

            return false;
        }

        if (isEmpty(this.pairs)) {
            boolean match = ignoreEmpty || isEmpty(pairs);

            log.info("Request params are empty. Queries match returned {}.", match);

            return match;
        }

        boolean match = this.pairs.entrySet().stream()
                .allMatch(e -> {
                    String key = e.getKey();
                    Stream<String> valuesFromRequest = (e.getValue() == null) ? empty()
                            : Arrays.stream(e.getValue()).sorted();
                    List<String> val2 = pairs.get(key);
                    List<String> valuesFromRequestProperties = (val2 == null) ? emptyList()
                            : val2.stream().sorted().collect(toList());

                    boolean m = valuesFromRequest.allMatch(v1 -> {
                        return (v1 == null && valuesFromRequestProperties.isEmpty())
                                || (v1 != null && !valuesFromRequestProperties.contains(v1) && ignoreUnknown)
                                || (v1 != null && Objects.equals(v1, valuesFromRequestProperties.get(
                                valuesFromRequestProperties.indexOf(v1))));
                    });

                    log.info("Comparing query params {}:[{}] with {}:[{}].",
                            key, valuesFromRequest, key, valuesFromRequestProperties);

                    return m;
                });

        log.info("Queries match returned {}.", match);

        return match;
    }
}
