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
