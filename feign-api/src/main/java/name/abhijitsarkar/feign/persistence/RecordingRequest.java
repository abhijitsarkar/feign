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

package name.abhijitsarkar.feign.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import name.abhijitsarkar.feign.Request;
import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * @author Abhijit Sarkar
 */
@Getter
@NoArgsConstructor
@ToString
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.ImmutableField", "PMD.SingularField"})
public class RecordingRequest extends Request {
    @Id
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
