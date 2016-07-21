package name.abhijitsarkar.feign.core

import name.abhijitsarkar.feign.core.matcher._
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.{Bean, Configuration}

/**
  * @author Abhijit Sarkar
  */
@Configuration
@ConditionalOnProperty(prefix = "feign.matchers", name = Array("disable"), havingValue = "false", matchIfMissing = true)
class MatchersAutoConfiguration {
  @Bean def defaultPathMatcher = new DefaultPathMatcher

  @Bean def defaultMethodMatcher = new DefaultMethodMatcher

  @Bean def defaultQueriesMatcher = new DefaultQueriesMatcher

  @Bean def defaultHeadersMatcher = new DefaultHeadersMatcher

  @Bean def defaultBodyMatcher = new DefaultBodyMatcher
}
