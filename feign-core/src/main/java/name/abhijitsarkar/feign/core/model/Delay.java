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

/**
 * @author Abhijit Sarkar
 */
@Getter
public class Delay {
    private Long delayMillis;
    private DelayStrategy delayStrategy;

    public Delay() {
        setDelayMillis(null);
        setDelayStrategy(null);
    }

    public void setDelayMillis(Long delayMillis) {
        this.delayMillis = delayMillis == null ? Long.valueOf(0L) : delayMillis;
    }

    public void setDelayStrategy(DelayStrategy delayStrategy) {
        this.delayStrategy = delayStrategy == null ? DelayStrategy.CONSTANT : delayStrategy;
    }

    public long effectiveDelay(int retryCount) {
        if (retryCount < 1) {
            return 0L;
        }

        switch (delayStrategy) {
            case WITH_LINEAR_BACK_OFF:
                return retryCount * delayMillis;
            case WITH_EXPONENTIAL_BACK_OFF:
                return (long) Math.pow(delayMillis, retryCount);
            default:
                return delayMillis;
        }
    }
}
