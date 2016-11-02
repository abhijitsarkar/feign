package name.abhijitsarkar.feign.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebReactive;
import org.springframework.web.reactive.config.WebReactiveConfigurer;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;

import java.util.List;

/**
 * @author Abhijit Sarkar
 */
@Configuration
@ComponentScan
@EnableWebReactive
public class FeignWebAutoConfiguration implements WebReactiveConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestHandlerMethodArgumentResolver());
    }
}
