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

package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import lombok.Setter;

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
    @Setter
    private Delay delay;

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
