package name.abhijitsarkar.feign.core.service;

import name.abhijitsarkar.feign.core.domain.Request;
import name.abhijitsarkar.feign.core.model.FeignMapping;

import java.util.Optional;

/**
 * @author Abhijit Sarkar
 */
public interface RecordingService {
    void record(Request request, Optional<FeignMapping> mapping);
}
