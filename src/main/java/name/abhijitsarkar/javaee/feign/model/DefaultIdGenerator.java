package name.abhijitsarkar.javaee.feign.model;

import name.abhijitsarkar.javaee.feign.domain.Request;

import java.util.UUID;

/**
 * @author Abhijit Sarkar
 */
public class DefaultIdGenerator implements IdGenerator {
    @Override
    public String id(Request request) {
        return UUID.randomUUID().toString();
    }
}
