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
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Abhijit Sarkar
 */
@Getter
@Setter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class Body extends AbstractIgnorableRequestProperties {
    private String raw;
    private String url;
    private String classpath;

    @SuppressWarnings({"PMD.ConfusingTernary", "PMD.PreserveStackTrace"})
    public String getContent() {
        validate();

        if (!isEmpty(raw)) {
            return raw;
        } else if (!isEmpty(url) || !isEmpty(classpath)) {
            String resource = Stream.of(url, classpath)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .get();
            try (InputStream io = new ClassPathResource(resource).getInputStream()) {
                return copyToString(io, UTF_8);
            } catch (IOException e) {
                try (InputStream io = new UrlResource(resource).getInputStream()) {
                    return copyToString(io, UTF_8);
                } catch (IOException ex) {
                    throw new UncheckedIOException(
                            String.format("Failed to read body from resource: %s.", resource), ex);
                }
            }
        }

        return "";
    }

    private void validate() {
        long count = Stream.of(raw, url, classpath)
                .filter(Objects::nonNull)
                .count();

        Assert.state(count <= 1, "Ambiguous request body declaration.");
    }
}
