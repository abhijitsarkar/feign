package name.abhijitsarkar.feign.core;

import name.abhijitsarkar.feign.core.matcher.DefaultBodyMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultHeadersMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultMethodMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultPathMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultQueriesMatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Abhijit Sarkar
 */
@Configuration
@ConditionalOnProperty(prefix = "feign.matchers", name = "enable",
        havingValue = "true", matchIfMissing = true)
public class MatchersAutoConfiguration {
    @Bean
    DefaultPathMatcher defaultPathMatcher() {
        return new DefaultPathMatcher();
    }

    @Bean
    DefaultMethodMatcher defaultMethodMatcher() {
        return new DefaultMethodMatcher();
    }

    @Bean
    DefaultQueriesMatcher defaultQueriesMatcher() {
        return new DefaultQueriesMatcher();
    }

    @Bean
    DefaultHeadersMatcher defaultHeadersMatcher() {
        return new DefaultHeadersMatcher();
    }

    @Bean
    DefaultBodyMatcher defaultBodyMatcher() {
        return new DefaultBodyMatcher();
    }
}
