package name.abhijitsarkar.feign.persistence.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.IdGenerator;
import name.abhijitsarkar.feign.RecordingService;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.persistence.domain.MongoDbRecordingRequest;
import name.abhijitsarkar.feign.persistence.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
public class MongoDbRecordingService implements RecordingService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    IdGenerator idGenerator;

    @EventListener
    public void record(PayloadApplicationEvent<Request> requestEvent) {
        Request request = requestEvent.getPayload();

        String id = idGenerator.id(request);

        MongoDbRecordingRequest recordingRequest = new MongoDbRecordingRequest(request, id);

        log.info("Recording request with id: {}.", id);

        requestRepository.save(recordingRequest);
    }
}
