package name.abhijitsarkar.feign

import name.abhijitsarkar.feign.core.FeignCoreAutoConfiguration
import name.abhijitsarkar.feign.persistence.FeignPersistenceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

/**
 * @author Abhijit Sarkar
 */
@Configuration
@Import([FeignCoreAutoConfiguration, FeignPersistenceAutoConfiguration])
class FeignConfiguration {

    @Bean
    @Profile('p2')
    IdGenerator idGenerator() {
        return new ConstantIdGenerator()
    }

    @Bean
    @Profile('p3')
    AlwaysTrueMatcher alwaysTrueMatcher() {
        return new AlwaysTrueMatcher()
    }
}
