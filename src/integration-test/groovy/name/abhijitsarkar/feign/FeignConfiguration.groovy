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

package name.abhijitsarkar.feign

import name.abhijitsarkar.feign.core.FeignCoreAutoConfiguration
import name.abhijitsarkar.feign.matcher.AlwaysTrueMatcher
import name.abhijitsarkar.feign.persistence.ConstantIdGenerator
import name.abhijitsarkar.feign.persistence.FeignPersistenceAutoConfiguration
import name.abhijitsarkar.feign.persistence.IdGenerator
import name.abhijitsarkar.feign.web.FeignWebAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

/**
 * @author Abhijit Sarkar
 */
@Configuration
@Import([FeignCoreAutoConfiguration, FeignPersistenceAutoConfiguration, FeignWebAutoConfiguration])
class FeignConfiguration {

    @Bean
    @Profile('p2')
    IdGenerator idGenerator() {
        return new ConstantIdGenerator()
    }

    @Bean
    @Profile('p3')
    AlwaysTrueMatcher alwaysTrueMatcher() {
        return new AlwaysTrueMatcher()
    }
}
