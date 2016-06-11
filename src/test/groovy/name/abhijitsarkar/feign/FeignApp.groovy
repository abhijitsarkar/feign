package name.abhijitsarkar.feign

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration

/**
 * @author Abhijit Sarkar
 */
@Configuration
@EnableAutoConfiguration
class FeignApp {
    static void main(String[] args) {
        SpringApplication.run(FeignApp, args)
    }
}
