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
package uk.gov.gchq.palisade.service.queryscope.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;

import uk.gov.gchq.palisade.Context;
import uk.gov.gchq.palisade.Generated;
import uk.gov.gchq.palisade.resource.LeafResource;
import uk.gov.gchq.palisade.resource.Resource;
import uk.gov.gchq.palisade.rule.Rules;
import uk.gov.gchq.palisade.service.queryscope.response.common.domain.User;

import java.util.Objects;
import java.util.StringJoiner;


/**
 * Represents the original data that has been sent from the client to Palisade Service for a request to access data.
 * This data will be forwarded to a set of services with each contributing to the processing of this request.
 * This version represents the input for query-scope-service request for defining the filtered adaptation of this request.
 * Next in the sequence will be the output for the query-scope-service with the filtered version of the request.
 * Note there are two classes that effectively represent the same data but represent a different stage of the process.
 * uk.gov.gchq.palisade.service.policy.response.PolicyResponse is the output from the policy-service.
 * uk.gov.gchq.palisade.service.queryscope.request.QueryScopeRequest is the input for the query-scope-service.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class QueryScopeRequest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final JsonNode context;  // Json Node representation of the Context
    private final JsonNode user;  //Json Node representation of the User
    private final JsonNode resources; // Json Node representation of the Resources
    private final JsonNode rules; // Json Node representation of the Rules

    @JsonCreator
    private QueryScopeRequest(
            final @JsonProperty("context") JsonNode context,
            final @JsonProperty("user") JsonNode user,
            final @JsonProperty("resources") JsonNode resources,
            final @JsonProperty("rules") JsonNode rules) {


        Assert.notNull(context, "Context cannot be null");
        Assert.notNull(user, "User cannot be null");
        Assert.notNull(resources, "Resources cannot be null");
        Assert.notNull(rules, "Rules cannot be null");

        this.context = context;
        this.user = user;
        this.resources = resources;
        this.rules = rules;
    }

    @Generated
    public Context getContext() throws JsonProcessingException {
        return MAPPER.treeToValue(this.context, Context.class);
    }

    @Generated
    public User getUser() throws JsonProcessingException {
        return MAPPER.treeToValue(this.user, User.class);
    }

    @Generated
    public LeafResource getResource() throws JsonProcessingException {
        return MAPPER.treeToValue(this.resources, LeafResource.class);
    }

    @Generated
    public Rules getRules() throws JsonProcessingException {
        return MAPPER.treeToValue(this.rules, Rules.class);
    }

    @Override
    @Generated
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryScopeRequest)) {
            return false;
        }
        QueryScopeRequest that = (QueryScopeRequest) o;
        return context.equals(that.context) &&
                user.equals(that.user) &&
                resources.equals(that.resources) &&
                rules.equals(that.rules);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(context, user, resources, rules);
    }

    @Override
    @Generated
    public String toString() {
        return new StringJoiner(", ", QueryScopeRequest.class.getSimpleName() + "[", "]")
                .add("context=" + context)
                .add("user=" + user)
                .add("resources=" + resources)
                .add("rules=" + rules)
                .add(super.toString())
                .toString();
    }

    /**
     * Builder class for the creation of instances of the QueryScopeRequest.  This is a variant of the Fluent Builder
     * which will use Java Objects or JsonNodes equivalents for the components in the build.
     */
    public static class Builder {
        private JsonNode context;
        private JsonNode user;
        private JsonNode resources;
        private JsonNode rules;

        public static IContext create() {
            return context -> user -> resources -> rules ->
                    new QueryScopeRequest(context, user, resources, rules);
        }

        interface IContext {
            default IUser withContext(Context context) {
                return withContextNode(MAPPER.valueToTree(context));
            }

            IUser withContextNode(JsonNode context);

        }

        interface IUser {
            default IResource withUser(User user) {
                return withUserNode(MAPPER.valueToTree(user));
            }

            IResource withUserNode(JsonNode context);
        }

        interface IResource {
            default IRules withResource(Resource resource) {
                return withResourceNode(MAPPER.valueToTree(resource));
            }

            IRules withResourceNode(JsonNode resource);
        }

        interface IRules {
            default QueryScopeRequest withRules(Rules rules) {
                return withRulesNode(MAPPER.valueToTree(rules));
            }

            QueryScopeRequest withRulesNode(JsonNode rules);
        }

    }

}