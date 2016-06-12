package name.abhijitsarkar.feign
/**
 * @author Abhijit Sarkar
 */

class ConstantIdGenerator implements IdGenerator {
    @Override
    String id(Request request) {
        return '1'
    }
}
