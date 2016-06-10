package name.abhijitsarkar.javaee.feign.matcher;

import lombok.RequiredArgsConstructor;
import name.abhijitsarkar.javaee.feign.model.Body;
import name.abhijitsarkar.javaee.feign.model.RequestProperties;

import java.util.function.Predicate;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@RequiredArgsConstructor
public class BodyMatcher implements Predicate<RequestProperties> {
    private final String body;

    @Override
    public boolean test(RequestProperties requestProperties) {
        Body body = requestProperties.getBody();

        if (isEmpty(body.toString())) {
            return true;
        }

        // TODO
        if (!isEmpty(body.getRaw())) {
            return this.body.matches(body.getRaw());
        } else if (!isEmpty(body.getFile())) {
            return false;
        } else if (!isEmpty(body.getClasspath())) {
            return false;
        } else if (!isEmpty(body.getJsonPath())) {
            return false;
        } else if (!isEmpty(body.getXPath())) {
            return false;
        } else {
            return false;
        }
    }
}
