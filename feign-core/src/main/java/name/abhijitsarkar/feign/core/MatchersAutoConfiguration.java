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

package name.abhijitsarkar.feign.core;

import name.abhijitsarkar.feign.core.matcher.DefaultBodyMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultHeadersMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultMethodMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultPathMatcher;
import name.abhijitsarkar.feign.core.matcher.DefaultQueriesMatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Abhijit Sarkar
 */
@Configuration
@ConditionalOnProperty(prefix = "feign.matchers", name = "disable",
        havingValue = "false", matchIfMissing = true)
public class MatchersAutoConfiguration {
    @Bean
    DefaultPathMatcher defaultPathMatcher() {
        return new DefaultPathMatcher();
    }

    @Bean
    DefaultMethodMatcher defaultMethodMatcher() {
        return new DefaultMethodMatcher();
    }

    @Bean
    DefaultQueriesMatcher defaultQueriesMatcher() {
        return new DefaultQueriesMatcher();
    }

    @Bean
    DefaultHeadersMatcher defaultHeadersMatcher() {
        return new DefaultHeadersMatcher();
    }

    @Bean
    DefaultBodyMatcher defaultBodyMatcher() {
        return new DefaultBodyMatcher();
    }
}
