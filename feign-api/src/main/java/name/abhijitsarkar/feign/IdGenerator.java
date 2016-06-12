package name.abhijitsarkar.feign;

/**
 * @author Abhijit Sarkar
 */
public interface IdGenerator {
    String id(Request request);
}
