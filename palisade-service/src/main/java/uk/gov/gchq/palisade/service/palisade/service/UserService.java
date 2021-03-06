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

import uk.gov.gchq.palisade.User;
import uk.gov.gchq.palisade.UserId;
import uk.gov.gchq.palisade.service.Service;
import uk.gov.gchq.palisade.service.palisade.request.GetUserRequest;
import uk.gov.gchq.palisade.service.palisade.web.UserClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

/**
 * UserService which implements {@link Service} and uses Feign within {@link UserClient} to send rest requests to the User Service
 */
public class UserService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserClient client;

    private final Executor executor;

    /**
     * Instantiates a new User service.
     *
     * @param userClient the user client interface for the User Service
     * @param executor   the executor
     */
    public UserService(final UserClient userClient, final Executor executor) {
        this.client = userClient;
        this.executor = executor;
    }

    /**
     * Makes a call to userClient and gets the user by userId contained in the request.
     * If the requested {@link UserId} doesn't exist in this {@link UserService} then an exception will be thrown.
     *
     * @param request the request
     * @return the user
     */
    public CompletableFuture<User> getUser(final GetUserRequest request) {
        LOGGER.debug("Getting user from user service: {}", request);

        CompletionStage<User> user;
        try {
            LOGGER.info("User request: {}", request);
            user = CompletableFuture.supplyAsync(() -> {
                User response = client.getUser(request);
                LOGGER.info("Got user: {}", response);
                return response;
            }, this.executor);
        } catch (Exception ex) {
            LOGGER.error("Failed to get user: {}", ex.getMessage());
            throw new RuntimeException(ex); //rethrow the exception
        }

        return user.toCompletableFuture();
    }

}
