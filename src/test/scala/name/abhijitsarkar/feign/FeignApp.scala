package name.abhijitsarkar.feign

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration

/**
  * @author Abhijit Sarkar
  */
object FeignApp extends App {
  SpringApplication.run(classOf[FeignApp])
}

@Configuration
@EnableAutoConfiguration
class FeignApp
