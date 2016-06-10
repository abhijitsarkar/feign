package name.abhijitsarkar.javaee.feign.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.javaee.feign.domain.Request;
import name.abhijitsarkar.javaee.feign.model.FeignMapping;
import name.abhijitsarkar.javaee.feign.model.FeignProperties;
import name.abhijitsarkar.javaee.feign.model.RequestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Abhijit Sarkar
 */
@Service
@Slf4j
public class FeignService {
    @Autowired
    private FeignProperties feignProperties;

    @Autowired
    private RecordingService recordingService;

    public Optional<FeignMapping> findFeignMapping(Request request) {
        Optional<FeignMapping> feignMapping = feignProperties.getMappings().stream()
                .filter(mapping -> {
                    RequestProperties rp = mapping.getRequest();

                    Predicate pathMatcher = createMatcher(rp.getPath().getMatcher(),
                            new Object[]{request.getPath()},
                            String.class);

                    Predicate methodMatcher = createMatcher(rp.getMethod().getMatcher(),
                            new Object[]{request.getMethod()},
                            String.class);

                    Predicate queriesMatcher = createMatcher(rp.getQueries().getMatcher(),
                            new Object[]{request.getQueryParams()},
                            Map.class);

                    Predicate headersMatcher = createMatcher(rp.getHeaders().getMatcher(),
                            new Object[]{request.getHeaders()},
                            Map.class);

                    Predicate bodyMatcher = createMatcher(rp.getBody().getMatcher(),
                            new Object[]{request.getBody()},
                            String.class);

                    return pathMatcher
                            .and(methodMatcher)
                            .and(queriesMatcher)
                            .and(headersMatcher)
                            .and(bodyMatcher)
                            .test(rp);
                })
                .findFirst();

        recordingService.record(request, feignMapping);

        log.info("Feign mapping {} for path: {}.", feignMapping.isPresent() ? "found" : "not found", request.getPath());

        return feignMapping;
    }

    private <T extends Predicate<RequestProperties>> T createMatcher(Class<? extends Predicate<RequestProperties>> matcherClass,
                                                                     Object[] constructorArgs,
                                                                     Class<?>... constructorArgClasses) {
        try {
            Constructor<T> constructor = (Constructor<T>) matcherClass.getConstructor(constructorArgClasses);

            return constructor.newInstance(constructorArgs);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
