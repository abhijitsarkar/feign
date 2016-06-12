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
@ConditionalOnProperty(prefix = "feign.matchers", name = "enable",
        havingValue = "true", matchIfMissing = true)
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
