package name.abhijitsarkar.feign.persistence.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.core.domain.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;
import name.abhijitsarkar.feign.core.model.IdGenerator;
import name.abhijitsarkar.feign.core.service.RecordingService;
import name.abhijitsarkar.feign.persistence.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author Abhijit Sarkar
 */
@Service
@Slf4j
public class MongoDbRecordingService implements RecordingService {
    @Autowired
    private RequestRepository requestRepository;

    public void record(Request request, Optional<FeignMapping> mapping) {
        mapping.ifPresent(m -> {
            boolean record = m.getRequest().isRecord();

            if (!record) {
                return;
            }

            Class<? extends IdGenerator> clazz = m.getRequest().getIdGenerator();

            try {
                Method idGeneratorMethod = clazz.getDeclaredMethod("id", Request.class);
                Object id = idGeneratorMethod.invoke(clazz.newInstance(), request);

                log.info("Recording request with id: {}.", id);

                requestRepository.save(request.toBuilder()
                        .id(id.toString()).build());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
