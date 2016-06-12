package name.abhijitsarkar.feign.core.matcher;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.Queries;
import name.abhijitsarkar.feign.core.model.RequestProperties;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
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
@Slf4j
public class DefaultQueriesMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    public Boolean apply(Request request, FeignMapping feignMapping) {
        RequestProperties requestProperties = feignMapping.getRequest();
        Queries queries = requestProperties.getQueries();
        Map<String, String[]> queryParams = request.getQueryParams();

        Map<String, List<String>> pairs = queries.getPairs();

        Boolean globalIgnoreUnknown = feignMapping.isIgnoreUnknown();
        Boolean globalIgnoreEmpty = feignMapping.isIgnoreEmpty();
        Boolean globalIgnoreCase = feignMapping.isIgnoreCase();

        Boolean localIgnoreUnknown = queries.isIgnoreUnknown();
        Boolean localIgnoreEmpty = queries.isIgnoreEmpty();
        Boolean localIgnoreCase = queries.isIgnoreCase();

        Boolean ignoreUnknown = resolveIgnoredProperties(globalIgnoreUnknown, localIgnoreUnknown, Boolean.TRUE);
        Boolean ignoreEmpty = resolveIgnoredProperties(globalIgnoreEmpty, localIgnoreEmpty, Boolean.TRUE);
        Boolean ignoreCase = resolveIgnoredProperties(globalIgnoreCase, localIgnoreCase, Boolean.FALSE);

        if (!isEmpty(queryParams) && isEmpty(pairs) && !ignoreUnknown) {
            log.info("Query params are not empty but request property queries are, " +
                    "and ignoreUnknown is false. Queries match returned false.");

            return false;
        }

        if (isEmpty(queryParams)) {
            boolean match = ignoreEmpty || isEmpty(pairs);

            log.info("Request params are empty. Queries match returned {}.", match);

            return match;
        }

        Comparator<String> nullsFirstNaturalOrder = nullsFirst(naturalOrder());

        boolean match = queryParams.entrySet().stream()
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

                        boolean m1 = (v1 == null && valuesFromRequestProperties.isEmpty());
                        boolean m2 = (v1 != null && !isContainsMatch(valuesFromRequestProperties, v1, ignoreCase) && ignoreUnknown);
                        boolean m3 = isContainsMatch(valuesFromRequestProperties, v1, ignoreCase);

                        if (m1) {
                            log.info("Query param is null and queries are empty.");
                        }
                        if (m2) {
                            log.info("Query param is not null, no match found in queries and ignoreUnknown is true.");
                        }
                        if (m3) {
                            log.info("Match found.");
                        }

                        return m1 || m2 || m3;
                    });

                    return m;
                });

        log.info("Queries match returned {}.", match);

        return match;
    }

    private boolean resolveIgnoredProperties(Boolean global, Boolean local, Boolean defaultValue) {
        if (local != null) {
            return local;
        } else if (global != null) {
            return global;
        }

        return defaultValue;
    }

    private boolean isContainsMatch(List<String> valuesFromRequestProperties, String valueFromRequest, boolean ignoreCase) {
        Stream<String> values = valuesFromRequestProperties.stream()
                .map(Strings::nullToEmpty);

        if (ignoreCase) {
            return values.map(String::toLowerCase)
                    .anyMatch(v -> nullToEmpty(valueFromRequest).toLowerCase().matches(v));
        }

        return values.anyMatch(v -> nullToEmpty(valueFromRequest).matches(v));
    }

}
