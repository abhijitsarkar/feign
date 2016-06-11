package name.abhijitsarkar.feign.core.matcher;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.core.model.Queries;
import name.abhijitsarkar.feign.core.model.RequestProperties;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Collections.emptyList;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
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
        boolean ignoreCase = queries.isIgnoreCase();

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

        Comparator<String> nullsFirstNaturalOrder = nullsFirst(naturalOrder());

        boolean match = this.pairs.entrySet().stream()
                .allMatch(e -> {
                    String key = e.getKey();
                    Stream<String> valuesFromRequest = (e.getValue() == null) ? empty()
                            : Arrays.stream(e.getValue()).sorted(nullsFirstNaturalOrder);
                    List<String> val2 = pairs.get(key);
                    List<String> valuesFromRequestProperties = (val2 == null) ? emptyList()
                            : val2.stream().sorted(nullsFirstNaturalOrder).collect(toList());

                    boolean m = valuesFromRequest.allMatch(v1 -> {
                        log.info("Finding match for query param {}:[{}] in {}.",
                                key, v1, valuesFromRequestProperties);

                        return (v1 == null && valuesFromRequestProperties.isEmpty())
                                || (v1 != null && !isContainsMatch(valuesFromRequestProperties, v1, ignoreCase) && ignoreUnknown)
                                || (isContainsMatch(valuesFromRequestProperties, v1, ignoreCase));
                    });

                    return m;
                });

        log.info("Queries match returned {}.", match);

        return match;
    }

    private boolean isContainsMatch(List<String> valuesFromRequestProperties, String valueFromRequest, boolean ignoreCase) {
        Stream<String> values = valuesFromRequestProperties.stream()
                .map(Strings::nullToEmpty);

        if (ignoreCase) {
            return values.map(String::toLowerCase)
                    .anyMatch(v -> nullToEmpty(valueFromRequest).matches(v));
        }

        return values.anyMatch(v -> nullToEmpty(valueFromRequest).matches(v));
    }
}
