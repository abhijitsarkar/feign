package name.abhijitsarkar.feign

import name.abhijitsarkar.feign.core.FeignCoreAutoConfiguration
import name.abhijitsarkar.feign.matcher.AlwaysTrueMatcher
import name.abhijitsarkar.feign.persistence.{ConstantIdGenerator, FeignPersistenceAutoConfiguration}
import org.springframework.context.annotation.{Bean, Configuration, Import, Profile}

/**
  * @author Abhijit Sarkar
  */
@Configuration
@Import(Array(classOf[FeignCoreAutoConfiguration], classOf[FeignPersistenceAutoConfiguration]))
class FeignConfiguration {
  @Bean
  @Profile(Array("p2"))
  def idGenerator = new ConstantIdGenerator

  @Bean
  @Profile(Array("p3"))
  def alwaysTrueMatcher = new AlwaysTrueMatcher
}
