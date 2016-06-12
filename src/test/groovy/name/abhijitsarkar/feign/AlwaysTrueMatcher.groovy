package name.abhijitsarkar.feign

import name.abhijitsarkar.feign.core.model.FeignMapping

import java.util.function.BiFunction

/**
 * @author Abhijit Sarkar
 */
class AlwaysTrueMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    Boolean apply(Request request, FeignMapping feignMapping) {
        return true
    }
}
