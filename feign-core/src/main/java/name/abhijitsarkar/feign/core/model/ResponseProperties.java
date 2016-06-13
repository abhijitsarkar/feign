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

package name.abhijitsarkar.feign.core.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Abhijit Sarkar
 */
@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class ResponseProperties {
    private Integer status;
    private Map<String, String> headers;
    private Body body;

    public ResponseProperties() {
        setStatus(null);
        setHeaders(null);
        setBody(null);
    }

    public void setStatus(Integer status) {
        this.status = (status == null) ? Integer.valueOf(OK.value()) : status;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = (headers == null) ? new HashMap<>() : headers;
    }

    public void setBody(Body body) {
        this.body = (body == null) ? new Body() : body;
    }
}
