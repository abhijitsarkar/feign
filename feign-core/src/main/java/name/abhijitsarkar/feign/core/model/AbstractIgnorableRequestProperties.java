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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * @author Abhijit Sarkar
 */
@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod", "PMD.BeanMembersShouldSerialize"})
public abstract class AbstractIgnorableRequestProperties {
    private Boolean ignoreUnknown;
    private Boolean ignoreEmpty;
    private Boolean ignoreCase;

    public AbstractIgnorableRequestProperties() {
        setIgnoreCase(null);
        setIgnoreEmpty(null);
        setIgnoreUnknown(null);
    }

    public Boolean isIgnoreUnknown() {
        return ignoreUnknown;
    }

    public Boolean isIgnoreEmpty() {
        return ignoreEmpty;
    }

    public Boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = (ignoreCase == null) ? FALSE : ignoreCase;
    }

    public void setIgnoreUnknown(Boolean ignoreUnknown) {
        this.ignoreUnknown = (ignoreUnknown == null) ? TRUE : ignoreUnknown;
    }

    public void setIgnoreEmpty(Boolean ignoreEmpty) {
        this.ignoreEmpty = (ignoreEmpty == null) ? TRUE : ignoreEmpty;
    }
}
