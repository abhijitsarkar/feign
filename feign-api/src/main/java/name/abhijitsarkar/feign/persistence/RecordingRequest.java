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

package name.abhijitsarkar.feign.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import name.abhijitsarkar.feign.model.Request;
import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * @author Abhijit Sarkar
 */
@NoArgsConstructor
@ToString
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.ImmutableField", "PMD.SingularField"})
public class RecordingRequest extends Request {
    @Id
    @Getter
    private String id;

    public RecordingRequest(Request request, String id) {
        super();
        path = request.getPath();
        method = request.getMethod();
        queryParams = request.getQueryParams();
        headers = request.getHeaders();
        body = request.getBody();

        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RecordingRequest that = (RecordingRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
