/*
 * Copyright (c) 2016, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * License for more details.
 */

package name.abhijitsarkar.feign.web

import name.abhijitsarkar.feign.model.Request
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

    def "supports Request subclass"() {
        setup:
        MethodParameter parameter = Mock(MethodParameter)
        parameter.parameterType >> TestRequest

        expect:
        new RequestHandlerMethodArgumentResolver().supportsParameter(parameter)
    }

    private static final class TestRequest extends Request {

    }
}