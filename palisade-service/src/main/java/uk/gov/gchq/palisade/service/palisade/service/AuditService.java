/*
 * Copyright 2019 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.gchq.palisade.service.palisade.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.gchq.palisade.service.Service;
import uk.gov.gchq.palisade.service.palisade.request.AuditRequest;
import uk.gov.gchq.palisade.service.palisade.web.AuditClient;

/**
 * AuditService which implements {@link Service} and uses Feign within {@link AuditClient} to send audit requests via rest to the Audit Service
 */
public class AuditService implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    private final AuditClient client;

    /**
     * Instantiates a new Audit service
     *
     * @param auditClient the audit client rest interface for the Audit Service
     */
    public AuditService(final AuditClient auditClient) {
        this.client = auditClient;
    }

    /**
     * Audit boolean which calls the Audit Client to audit values by request and returns a boolean if the request has been successfully logged.
     *
     * @param request the request
     * @return the boolean
     */
    public Boolean audit(final AuditRequest request) {
        LOGGER.debug("Submitting audit to audit service: {}", request);

        Boolean response;
        try {
            LOGGER.info("Audit request: {}", request);
            response = this.client.audit(request);
            LOGGER.info("Audit response: {}", response);
        } catch (Exception ex) {
            LOGGER.error("Failed to log audit request: {}", ex.getMessage());
            throw new RuntimeException(ex);
        }

        return response;
    }

}
