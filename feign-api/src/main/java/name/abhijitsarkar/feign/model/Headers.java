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

import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * @author Abhijit Sarkar
 */

@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class Headers extends AbstractIgnorableRequestProperties {
    private Map<String, String> pairs;

    public Headers() {
        super();
        setPairs(null);
    }

    public void setPairs(Map<String, String> pairs) {
        this.pairs = (pairs == null) ? emptyMap() : pairs;
    }
}
