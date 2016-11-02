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
