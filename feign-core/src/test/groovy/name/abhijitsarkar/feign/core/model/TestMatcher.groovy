package name.abhijitsarkar.feign.core.model

import java.util.function.Predicate
/**
 * @author Abhijit Sarkar
 */
class TestMatcher implements  Predicate<RequestProperties> {
    @Override
    boolean test(RequestProperties requestProperties) {
        return false
    }
}
