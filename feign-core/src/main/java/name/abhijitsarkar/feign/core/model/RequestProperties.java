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

/**
 * @author Abhijit Sarkar
 */
@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class RequestProperties {
    private Path path;
    private Method method;
    private Queries queries;
    private Headers headers;
    private Body body;

    public RequestProperties() {
        setPath(null);
        setMethod(null);
        setQueries(null);
        setHeaders(null);
        setBody(null);
    }

    public void setPath(Path path) {
        this.path = (path == null) ? new Path() : path;
    }

    public void setMethod(Method method) {
        this.method = (method == null) ? new Method() : method;
    }

    public void setQueries(Queries queries) {
        this.queries = (queries == null) ? new Queries() : queries;
    }

    public void setHeaders(Headers headers) {
        this.headers = (headers == null) ? new Headers() : headers;
    }

    public void setBody(Body body) {
        this.body = (body == null) ? new Body() : body;
    }
}
