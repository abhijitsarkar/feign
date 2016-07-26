/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 */

package name.abhijitsarkar.feign.core

import name.abhijitsarkar.feign.core.matcher._
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.{Bean, Configuration}

/**
  * @author Abhijit Sarkar
  */
@Configuration
@ConditionalOnProperty(prefix = "feign.matchers", name = Array("disable"), havingValue = "false", matchIfMissing = true)
class MatchersAutoConfiguration {
  @Bean def defaultPathMatcher = new DefaultPathMatcher

  @Bean def defaultMethodMatcher = new DefaultMethodMatcher

  @Bean def defaultQueriesMatcher = new DefaultQueriesMatcher

  @Bean def defaultHeadersMatcher = new DefaultHeadersMatcher

  @Bean def defaultBodyMatcher = new DefaultBodyMatcher
}
