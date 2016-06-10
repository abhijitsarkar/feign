package name.abhijitsarkar.javaee.feign;

import name.abhijitsarkar.javaee.feign.web.RequestHandlerMethodArgumentResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author Abhijit Sarkar
 */
@Configuration
@ComponentScan
public class FeignAutoConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequestHandlerMethodArgumentResolver());
    }
}
