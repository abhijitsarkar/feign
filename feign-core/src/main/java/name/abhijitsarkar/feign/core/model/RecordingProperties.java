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
import lombok.Setter;
import name.abhijitsarkar.feign.persistence.IdGenerator;

import static java.lang.Boolean.FALSE;

/**
 * @author Abhijit Sarkar
 */
@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class RecordingProperties {
    private Boolean disable;

    @Setter
    private Class<? extends IdGenerator> idGenerator;

    public RecordingProperties() {
        setDisable(null);
    }

    public Boolean isDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = (disable == null) ? FALSE : disable;
    }
}
