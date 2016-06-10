package name.abhijitsarkar.javaee.feign.matcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.javaee.feign.model.RequestProperties;
import org.springframework.util.AntPathMatcher;

import java.util.function.Predicate;

/**
 * @author Abhijit Sarkar
 */
@RequiredArgsConstructor
@Slf4j
public class PathMatcher implements Predicate<RequestProperties> {
    @NonNull
    private final String path;

    private final org.springframework.util.PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean test(RequestProperties requestProperties) {
        String path = requestProperties.getPath().getUri();

        boolean match = pathMatcher.match(path, this.path);

        log.info("Comparing request path: {} with: {}.", this.path, path);
        log.info("Path match returned: {}.", match);

        return match;
    }
}
