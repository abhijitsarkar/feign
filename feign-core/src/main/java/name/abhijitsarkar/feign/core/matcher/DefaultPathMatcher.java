package name.abhijitsarkar.feign.core.matcher;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.RequestProperties;
import org.springframework.util.AntPathMatcher;

import java.util.function.BiFunction;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class DefaultPathMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    private final org.springframework.util.PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Boolean apply(Request request, FeignMapping feignMapping) {
        RequestProperties requestProperties = feignMapping.getRequest();
        String requestPath = request.getPath();

        String path = requestProperties.getPath().getUri();
        boolean ignoreCase = requestProperties.getPath().isIgnoreCase();

        boolean match = ignoreCase ? pathMatcher.match(path.toLowerCase(), requestPath.toLowerCase()) :
                pathMatcher.match(path, requestPath);

        log.info("Comparing request path: {} with uri: {}.", requestPath, path);
        log.info("Path match returned: {}.", match);

        return match;
    }
}
