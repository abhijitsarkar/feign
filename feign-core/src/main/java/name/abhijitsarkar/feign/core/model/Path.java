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

import static java.lang.Boolean.FALSE;

/**
 * @author Abhijit Sarkar
 */

public class Path {
    public static final String WILDCARD_PATTERN = "/**";

    @Getter
    private String uri;
    private Boolean ignoreCase;

    public Path() {
        setUri(uri);
        setIgnoreCase(ignoreCase);
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setUri(String uri) {
        this.uri = (uri == null) ? WILDCARD_PATTERN : uri;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }
}
