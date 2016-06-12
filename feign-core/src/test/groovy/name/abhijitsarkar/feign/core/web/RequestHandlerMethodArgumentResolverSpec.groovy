package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.Request
import org.springframework.web.context.request.NativeWebRequest
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

import static java.util.Collections.enumeration

/**
 * @author Abhijit Sarkar
 */
class RequestHandlerMethodArgumentResolverSpec extends Specification {
    def "builds request from HttpServletRequest"() {
        setup:
        def httpServletRequest = Mock(HttpServletRequest)
        httpServletRequest.headerNames >> enumeration(['a'])
        httpServletRequest.getHeader('a') >> 'b'
        httpServletRequest.reader >> new BufferedReader(new StringReader('body'))
        httpServletRequest.servletPath >> '/abc'
        httpServletRequest.method >> 'GET'
        httpServletRequest.parameterMap >> ['a': 'b']

        def webRequest = Mock(NativeWebRequest)
        webRequest.nativeRequest >> httpServletRequest

        when:
        Request request = new RequestHandlerMethodArgumentResolver().resolveArgument(
                null, null, webRequest, null)

        then:
        request.path == httpServletRequest.servletPath
        request.method == httpServletRequest.method
        request.queryParams == httpServletRequest.parameterMap
        request.headers == ['a': 'b']
        request.body == 'body'
    }
}