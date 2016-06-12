/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *  *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 *
 */

package name.abhijitsarkar.feign.core.web

import name.abhijitsarkar.feign.Request
import org.springframework.core.MethodParameter
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

    def "supports Request"() {
        setup:
        MethodParameter parameter = Mock(MethodParameter)
        parameter.parameterType >> Request

        expect:
        new RequestHandlerMethodArgumentResolver().supportsParameter(parameter)
    }

    def "supports Request subclasses"() {
        setup:
        MethodParameter parameter = Mock(MethodParameter)
        parameter.parameterType >> TestRequest

        expect:
        new RequestHandlerMethodArgumentResolver().supportsParameter(parameter)
    }

    private static final class TestRequest extends Request {

    }
}