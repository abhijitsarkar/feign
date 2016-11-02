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

package name.abhijitsarkar.feign.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abhijit Sarkar
 */
@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class FeignMapping {
    private RequestProperties request;
    private List<ResponseProperties> response;

    public FeignMapping() {
        super();
        setRequest(null);
        setResponse(null);
    }

    public void setRequest(RequestProperties request) {
        this.request = (request == null) ? new RequestProperties() : request;
    }

    public void setResponse(List<ResponseProperties> response) {
        this.response = (response == null) ? new ArrayList<>() : response;
    }
}
