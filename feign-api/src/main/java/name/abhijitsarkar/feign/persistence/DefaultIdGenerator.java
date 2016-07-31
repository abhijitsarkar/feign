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


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
@ToString
@SuppressWarnings({"PMD.ShortMethodName"})
public class DefaultIdGenerator implements IdGenerator {
    @Override
    public String id(Request request) {
        Pattern pattern = Pattern.compile("^(?:/?)([^/]+)(?:.*)$");
        Matcher matcher = pattern.matcher(request.getPath());

        String prefix = matcher.matches() ? matcher.group(1) : "unknown";

        return String.format("%s-%s",
                prefix,
                request.getPath().hashCode());
    }
}
