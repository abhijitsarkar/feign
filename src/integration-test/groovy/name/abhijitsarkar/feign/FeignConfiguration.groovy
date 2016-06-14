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

package name.abhijitsarkar.feign

import name.abhijitsarkar.feign.core.FeignCoreAutoConfiguration
import name.abhijitsarkar.feign.matcher.AlwaysTrueMatcher
import name.abhijitsarkar.feign.persistence.ConstantIdGenerator
import name.abhijitsarkar.feign.persistence.FeignPersistenceAutoConfiguration
import name.abhijitsarkar.feign.persistence.IdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

/**
 * @author Abhijit Sarkar
 */
@Configuration
@Import([FeignCoreAutoConfiguration, FeignPersistenceAutoConfiguration])
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
