package name.abhijitsarkar.feign.core.matcher;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.RequestProperties;

import java.util.function.BiFunction;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultMethodMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    public Boolean apply(Request request, FeignMapping feignMapping) {
        RequestProperties requestProperties = feignMapping.getRequest();
        String requestMethod = request.getMethod();

        String method = requestProperties.getMethod().getName();
        boolean ignoreCase = requestProperties.getMethod().isIgnoreCase();

        boolean match = ignoreCase ? requestMethod.toLowerCase().matches(method.toLowerCase()) :
                requestMethod.matches(method);

        log.info("Comparing request method: {} with: {}.", requestMethod, method);
        log.info("Method match returned {}.", match);

        return match;
    }
}
