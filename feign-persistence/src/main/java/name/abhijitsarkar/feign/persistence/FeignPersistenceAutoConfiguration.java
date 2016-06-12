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

package name.abhijitsarkar.feign.persistence;

import name.abhijitsarkar.feign.IdGenerator;
import name.abhijitsarkar.feign.RecordingService;
import name.abhijitsarkar.feign.persistence.domain.DefaultIdGenerator;
import name.abhijitsarkar.feign.persistence.service.MongoDbRecordingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Abhijit Sarkar
 */
@Configuration
@ComponentScan
public class FeignPersistenceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(IdGenerator.class)
    IdGenerator defaultIdGenerator() {
        return new DefaultIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(RecordingService.class)
    MongoDbRecordingService recordingService() {
        return new MongoDbRecordingService();
    }
}
