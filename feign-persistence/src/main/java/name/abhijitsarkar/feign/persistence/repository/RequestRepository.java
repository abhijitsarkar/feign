package name.abhijitsarkar.feign.persistence.repository;

import name.abhijitsarkar.feign.persistence.domain.MongoDbRecordingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Abhijit Sarkar
 */
@RepositoryRestResource(collectionResourceRel = "requests", path = "requests")
public interface RequestRepository extends MongoRepository<MongoDbRecordingRequest, String> {
}
