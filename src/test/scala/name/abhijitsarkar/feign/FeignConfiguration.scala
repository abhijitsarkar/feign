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

package name.abhijitsarkar.feign

import name.abhijitsarkar.feign.core.FeignCoreAutoConfiguration
import name.abhijitsarkar.feign.matcher.AlwaysTrueMatcher
import name.abhijitsarkar.feign.persistence.{ConstantIdGenerator, FeignPersistenceAutoConfiguration}
import org.springframework.context.annotation.{Bean, Configuration, Import, Profile}

/**
  * @author Abhijit Sarkar
  */
@Configuration
@Import(Array(classOf[FeignCoreAutoConfiguration], classOf[FeignPersistenceAutoConfiguration]))
class FeignConfiguration {
  @Bean
  @Profile(Array("p2"))
  def idGenerator = new ConstantIdGenerator

  @Bean
  @Profile(Array("p3"))
  def alwaysTrueMatcher = new AlwaysTrueMatcher
}