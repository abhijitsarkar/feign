package name.abhijitsarkar.javaee.feign.model;

import lombok.Getter;
import name.abhijitsarkar.javaee.feign.matcher.MethodMatcher;

import java.util.function.Predicate;

/**
 * @author Abhijit Sarkar
 */
@Getter
public class Method {
    String name;
    private Class<? extends Predicate<RequestProperties>> matcher;

    public Method() {
        setName(name);
        setMatcher(matcher);
    }

    public void setName(String name) {
        this.name = (name == null) ? ".*" : name;
    }

    public void setMatcher(Class<? extends Predicate<RequestProperties>> matcher) {
        this.matcher = (matcher == null) ? MethodMatcher.class : matcher;
    }
}
