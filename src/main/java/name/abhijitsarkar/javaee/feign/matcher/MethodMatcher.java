package name.abhijitsarkar.javaee.feign.matcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.javaee.feign.model.RequestProperties;

import java.util.function.Predicate;

/**
 * @author Abhijit Sarkar
 */
@RequiredArgsConstructor
@Slf4j
public class MethodMatcher implements Predicate<RequestProperties> {
    @NonNull
    private final String method;

    @Override
    public boolean test(RequestProperties requestProperties) {
        String method = requestProperties.getMethod().getName();

        boolean match = this.method.matches(method);

        log.info("Comparing request method: {} with: {}.", this.method, method);
        log.info("Method match returned {}.", match);

        return match;
    }
}
