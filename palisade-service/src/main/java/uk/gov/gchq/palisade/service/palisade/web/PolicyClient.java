/*
 * Copyright 2020 Crown Copyright
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
package uk.gov.gchq.palisade.service.palisade.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import uk.gov.gchq.palisade.resource.LeafResource;
import uk.gov.gchq.palisade.rule.Rules;
import uk.gov.gchq.palisade.service.palisade.request.GetPolicyRequest;

import java.util.Map;

/**
 * The interface Policy client which uses Feign and uses services urls if provided, otherwise discovery by name with eureka
 */
@FeignClient(name = "policy-service", url = "${web.client.policy-service}")
public interface PolicyClient {
    /**
     * Sends a post rest request to the PolicyService and returns a Completable future of LeafResources and rules
     *
     * @param request the request
     * @return the policy sync
     */
    @PostMapping(path = "/getPolicySync", consumes = "application/json", produces = "application/json")
    Map<LeafResource, Rules> getPolicySync(@RequestBody final GetPolicyRequest request);
}
