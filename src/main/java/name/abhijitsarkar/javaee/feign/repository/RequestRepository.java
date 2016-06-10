package name.abhijitsarkar.javaee.feign.repository;

import name.abhijitsarkar.javaee.feign.domain.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Abhijit Sarkar
 */
public interface RequestRepository extends MongoRepository<Request, String> {
}
