package name.abhijitsarkar.feign.core.model;

import name.abhijitsarkar.feign.core.domain.Request;

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
