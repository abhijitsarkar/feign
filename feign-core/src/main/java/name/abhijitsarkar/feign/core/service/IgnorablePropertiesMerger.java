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

package name.abhijitsarkar.feign.core.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.core.model.AbstractIgnorableRequestProperties;
import name.abhijitsarkar.feign.core.model.FeignProperties;
import name.abhijitsarkar.feign.core.model.RequestProperties;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.util.ReflectionUtils.invokeMethod;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
@SuppressWarnings({"PMD.ConfusingTernary", "PMD.NullAssignment"})
public class IgnorablePropertiesMerger {
    public void merge(RequestProperties requestProperties, FeignProperties feignProperties) {
        List<PropertyDescriptor> globalIgnoreProperties =
                Arrays.asList(getPropertyDescriptors(AbstractIgnorableRequestProperties.class));

        Stream.of(
                requestProperties.getPath(),
                requestProperties.getMethod(),
                requestProperties.getQueries(),
                requestProperties.getHeaders(),
                requestProperties.getBody())
                .filter(Objects::nonNull)
                .forEach(requestProperty -> {
                    Class<?> clazz = requestProperty.getClass();
                    log.debug("Introspecting class: {}.", clazz.getName());

                    Arrays.stream(getPropertyDescriptors(clazz))
                            .forEach(property -> {
                                String name = property.getName();

                                globalIgnoreProperties.stream()
                                        .filter(globalProperty -> !"class".equals(name)
                                                && globalProperty.getName().equals(name))
                                        .findFirst()
                                        .ifPresent(globalProperty -> {
                                            log.debug("Merging property: {}.", name);

                                            Method getter = property.getReadMethod();
                                            Method superGetter = globalProperty.getReadMethod();

                                            Object ignore = null;

                                            if (getter != null) {
                                                ignore = invokeMethod(getter, requestProperty);
                                            } else if (superGetter != null) {
                                                ignore = invokeMethod(superGetter, requestProperty);
                                            }

                                            if (ignore == null) {
                                                Object globalIgnore = (superGetter != null) ?
                                                        invokeMethod(superGetter, feignProperties) : null;
                                                Method setter = property.getWriteMethod();
                                                Method superSetter = globalProperty.getWriteMethod();

                                                if (setter != null) {
                                                    invokeMethod(setter, requestProperty, globalIgnore);
                                                } else if (superSetter != null) {
                                                    invokeMethod(superSetter, requestProperty, globalIgnore);
                                                }
                                            }
                                        });
                            });
                });
    }

    private PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        try {
            return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }

        return new PropertyDescriptor[]{};
    }
}
