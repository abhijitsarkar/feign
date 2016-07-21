package name.abhijitsarkar.feign.core

import java.util.{List => JavaList}

import name.abhijitsarkar.feign.core.web.RequestHandlerMethodArgumentResolver
import org.springframework.context.annotation.{ComponentScan, Configuration}
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
  * @author Abhijit Sarkar
  */
@Configuration
@ComponentScan
class FeignCoreAutoConfiguration extends WebMvcConfigurerAdapter {
  override def addArgumentResolvers(argumentResolvers: JavaList[HandlerMethodArgumentResolver]) {
    argumentResolvers.add(new RequestHandlerMethodArgumentResolver)
  }
}