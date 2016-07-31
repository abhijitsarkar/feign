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

package name.abhijitsarkar.feign.persistence.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.persistence.RecordingRequest;
import name.abhijitsarkar.feign.persistence.RecordingService;
import name.abhijitsarkar.feign.persistence.repository.MongoDbRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;

/**
 * @author Abhijit Sarkar
 */
@Slf4j
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public class MongoDbRecordingService implements RecordingService {
    @Autowired
    MongoDbRequestRepository mongoDbRequestRepository;

    @EventListener
    public void record(PayloadApplicationEvent<RecordingRequest> requestEvent) {
        RecordingRequest recordingRequest = requestEvent.getPayload();
        String id = recordingRequest.getId();

        log.info("Recording request with id: {}.", id);

        mongoDbRequestRepository.save(recordingRequest);
    }
}
