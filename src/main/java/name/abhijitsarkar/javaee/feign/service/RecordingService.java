package name.abhijitsarkar.javaee.feign.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.javaee.feign.domain.Request;
import name.abhijitsarkar.javaee.feign.model.FeignMapping;
import name.abhijitsarkar.javaee.feign.model.IdGenerator;
import name.abhijitsarkar.javaee.feign.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author Abhijit Sarkar
 */
@Service
@Slf4j
public class RecordingService {
    @Autowired
    private RequestRepository requestRepository;

    public void record(Request request, Optional<FeignMapping> mapping) {
        mapping.ifPresent(m -> {
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
