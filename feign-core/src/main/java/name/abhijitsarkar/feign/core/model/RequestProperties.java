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

/**
 * @author Abhijit Sarkar
 */
@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class RequestProperties {
    private RecordingProperties recording;
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
        setRecording(null);
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

    public void setRecording(RecordingProperties recording) {
        this.recording = (recording == null) ? new RecordingProperties() : recording;
    }
}
