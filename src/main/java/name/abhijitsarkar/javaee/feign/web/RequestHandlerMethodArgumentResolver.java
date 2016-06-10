package name.abhijitsarkar.javaee.feign.web;

import name.abhijitsarkar.javaee.feign.domain.Request;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static java.lang.System.lineSeparator;
import static java.util.Collections.list;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @author Abhijit Sarkar
 */
public class RequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Request.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Map<String, String> headers = list(request.getHeaderNames()).stream()
                .collect(toMap(name -> name, request::getHeader));

        String body = request.getReader().lines().collect(joining(lineSeparator()));

        return Request.builder()
                .path(request.getServletPath())
                .method(request.getMethod())
                .queryParams(request.getParameterMap())
                .headers(headers)
                .body(body)
                .build();
    }
}
