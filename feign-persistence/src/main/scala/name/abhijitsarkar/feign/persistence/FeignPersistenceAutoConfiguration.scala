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

package name.abhijitsarkar.feign.persistence

import name.abhijitsarkar.feign.persistence.repository.MongoDbRequestRepository
import name.abhijitsarkar.feign.persistence.service.MongoDbRecordingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}

/**
  * @author Abhijit Sarkar
  */
@Configuration
@ComponentScan
class FeignPersistenceAutoConfiguration {
  @Autowired
  var mongoDbRequestRepository: MongoDbRequestRepository = _

  @Bean
  @ConditionalOnProperty(prefix = "feign.recording", name = Array("disable"),
    havingValue = "false", matchIfMissing = true)
  def recordingService = new MongoDbRecordingService(mongoDbRequestRepository)
}
