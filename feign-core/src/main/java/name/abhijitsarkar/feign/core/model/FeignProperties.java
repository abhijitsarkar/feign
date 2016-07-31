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

package name.abhijitsarkar.feign.core.model;

import lombok.Getter;
import name.abhijitsarkar.feign.persistence.DefaultIdGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;

/**
 * @author Abhijit Sarkar
 */
@Component
@ConfigurationProperties(prefix = "feign")
@Getter
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.SingularField"})
public class FeignProperties extends AbstractIgnorableRequestProperties {
    private RecordingProperties recording;
    private List<FeignMapping> mappings;
    private Delay delay;

    @PostConstruct
    public void postConstruct() {
        setMappings(mappings);
        setRecording(recording);
        setDelay(delay);
        setIgnoreCase(null);
        setIgnoreUnknown(null);
        setIgnoreEmpty(null);
    }

    public void setMappings(List<FeignMapping> mappings) {
        this.mappings = (mappings == null) ? emptyList() : mappings;
    }

    public void setRecording(RecordingProperties recording) {
        this.recording = (recording == null) ? new RecordingProperties() : recording;

        if (this.recording.getIdGenerator() == null) {
            this.recording.setIdGenerator(DefaultIdGenerator.class);
        }
    }

    public void setDelay(Delay delay) {
        this.delay = delay == null ? new Delay() : delay;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        super.setIgnoreCase((ignoreCase == null) ? FALSE : ignoreCase);
    }

    public void setIgnoreUnknown(Boolean ignoreUnknown) {
        super.setIgnoreUnknown((ignoreUnknown == null) ? TRUE : ignoreUnknown);
    }

    public void setIgnoreEmpty(Boolean ignoreEmpty) {
        super.setIgnoreEmpty((ignoreEmpty == null) ? TRUE : ignoreEmpty);
    }
}
