package name.abhijitsarkar.feign.core.model;

import name.abhijitsarkar.feign.core.domain.Request;

/**
 * @author Abhijit Sarkar
 */
public interface IdGenerator {
    String id(Request request);
}
