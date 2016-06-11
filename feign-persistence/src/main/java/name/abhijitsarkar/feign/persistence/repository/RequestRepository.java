package name.abhijitsarkar.feign.persistence.repository;

import name.abhijitsarkar.feign.core.domain.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Abhijit Sarkar
 */
public interface RequestRepository extends MongoRepository<Request, String> {
}
