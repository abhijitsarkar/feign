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

import static java.lang.Boolean.FALSE;

/**
 * @author Abhijit Sarkar
 */
@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class Method {
    public static final String WILDCARD = ".*";

    private String name;

    private Boolean ignoreCase;

    public Method() {
        setName(null);
        setIgnoreCase(null);
    }

    public Boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setName(String name) {
        this.name = (name == null) ? WILDCARD : name;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }
}
