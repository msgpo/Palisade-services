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

package uk.gov.gchq.palisade.service.user.web;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import uk.gov.gchq.palisade.RequestId;
import uk.gov.gchq.palisade.User;
import uk.gov.gchq.palisade.service.user.config.StdUserConfiguration;
import uk.gov.gchq.palisade.service.user.request.AddUserRequest;
import uk.gov.gchq.palisade.service.user.request.GetUserRequest;
import uk.gov.gchq.palisade.service.user.service.MockUserService;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserControllerTest {
    public final UserController userController = new UserController(new MockUserService(), new StdUserConfiguration());

    private Logger logger;
    private ListAppender<ILoggingEvent> appender;

    @BeforeAll
    public void setup() {
        logger = (Logger) LoggerFactory.getLogger(UserController.class);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterAll
    public void tearDown() {
        logger.detachAppender(appender);
        appender.stop();
    }

    private List<String> getMessages(final Predicate<ILoggingEvent> predicate) {
        return appender.list.stream()
                .filter(predicate)
                .map(ILoggingEvent::getFormattedMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void addAndGetUser() {
        User user = new User().userId("add-user-request-id").addAuths(Collections.singleton("authorisation")).addRoles(Collections.singleton("role"));
        assertAll("addAndGetUser",
                () -> {
                    AddUserRequest addUserRequest = AddUserRequest.create(new RequestId().id("addUserRequest")).withUser(user);
                    assertTrue(userController.addUserRequest(addUserRequest));

                    // Executed only if the previous assertion is valid.
                    assertAll("addUserRequest",
                            () -> assertNotNull(user.getAuths(), "User auths should not be null"),
                            () -> assertNotNull(user.getRoles(), "User roles should be not be null")
                    );
                },
                () -> {
                    // Grouped assertion, so processed independently
                    // of results of first name assertions.
                    GetUserRequest getUserRequest = GetUserRequest.create(new RequestId().id("getUserRequest")).withUserId(user.getUserId());
                    User expected = userController.getUserRequest(getUserRequest);
                    assertEquals(user, expected);

                    // Executed only if the previous assertion is valid.
                    assertAll("getUserRequest",
                            () -> assertEquals(user.getAuths(), expected.getAuths(), "Auths should match"),
                            () -> assertEquals(user.getRoles(), expected.getRoles(), "Roles should match")
                    );
                },
                () -> {
                    // Grouped assertion, so processed independently
                    // of results of first name assertions.
                    List<String> debugMessages = getMessages(event -> event.getLevel() == Level.INFO);
                    assertNotEquals(0, debugMessages.size());
                    MatcherAssert.assertThat(debugMessages, Matchers.hasItems(
                            Matchers.containsString("Invoking AddUserRequest:"),
                            Matchers.anyOf(
                                    Matchers.containsString("Invoking GetUserRequest: GetUserRequest"))
                    ));
                }
        );
    }
}