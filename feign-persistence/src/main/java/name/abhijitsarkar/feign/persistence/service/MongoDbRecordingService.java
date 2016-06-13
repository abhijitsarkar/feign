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

package name.abhijitsarkar.feign.persistence.service;

import lombok.extern.slf4j.Slf4j;
import name.abhijitsarkar.feign.IdGenerator;
import name.abhijitsarkar.feign.RecordingService;
import name.abhijitsarkar.feign.Request;
import name.abhijitsarkar.feign.persistence.domain.MongoDbRecordingRequest;
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
    @Autowired
    IdGenerator idGenerator;

    @EventListener
    public void record(PayloadApplicationEvent<Request> requestEvent) {
        Request request = requestEvent.getPayload();

        String id = idGenerator.id(request);

        MongoDbRecordingRequest recordingRequest = new MongoDbRecordingRequest(request, id);

        log.info("Recording request with id: {}.", id);

        mongoDbRequestRepository.save(recordingRequest);
    }
}
