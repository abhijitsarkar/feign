package name.abhijitsarkar.feign.core.matcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.core.model.Body;
import name.abhijitsarkar.feign.core.model.RequestProperties;

import java.util.function.Predicate;

import static com.google.common.base.Strings.nullToEmpty;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@RequiredArgsConstructor
@Slf4j
public class BodyMatcher implements Predicate<RequestProperties> {
    private final String body;

    @Override
    public boolean test(RequestProperties requestProperties) {
        Body body = requestProperties.getBody();
        boolean ignoreEmpty = body.isIgnoreEmpty();
        boolean ignoreUnknown = body.isIgnoreUnknown();
        String content = nullToEmpty(body.getContent());

        if ((isEmpty(this.body) && isEmpty(content))
                || (isEmpty(this.body) && ignoreEmpty)
                || (!isEmpty(this.body) && isEmpty(content) && ignoreUnknown)) {
            return true;
        } else if (isEmpty(this.body)) {
            return false;
        }

        boolean ignoreCase = body.isIgnoreCase();

        boolean match = ignoreCase ? this.body.toLowerCase().matches(content.toLowerCase())
                : this.body.matches(content);

        log.info("Comparing request body: {} with: {}.", this.body, content);
        log.info("Body match returned: {}.", match);

        return match;
    }
}
