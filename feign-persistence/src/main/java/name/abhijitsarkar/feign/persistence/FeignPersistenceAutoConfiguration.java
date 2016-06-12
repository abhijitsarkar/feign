package name.abhijitsarkar.feign.persistence;

import name.abhijitsarkar.feign.IdGenerator;
import name.abhijitsarkar.feign.RecordingService;
import name.abhijitsarkar.feign.persistence.domain.DefaultIdGenerator;
import name.abhijitsarkar.feign.persistence.service.MongoDbRecordingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Abhijit Sarkar
 */
@Configuration
@ComponentScan
public class FeignPersistenceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(IdGenerator.class)
    IdGenerator defaultIdGenerator() {
        return new DefaultIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(RecordingService.class)
    MongoDbRecordingService recordingService() {
        return new MongoDbRecordingService();
    }
}
