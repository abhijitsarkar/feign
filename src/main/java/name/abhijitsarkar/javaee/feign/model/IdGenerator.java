package name.abhijitsarkar.javaee.feign.model;

import name.abhijitsarkar.javaee.feign.domain.Request;

/**
 * @author Abhijit Sarkar
 */
public interface IdGenerator {
    String id(Request request);
}
