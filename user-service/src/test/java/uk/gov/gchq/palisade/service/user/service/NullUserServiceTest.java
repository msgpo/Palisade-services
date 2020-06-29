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

package uk.gov.gchq.palisade.service.user.service;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.palisade.User;
import uk.gov.gchq.palisade.service.user.exception.NoSuchUserIdException;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NullUserServiceTest {
    NullUserService nullUserService = new NullUserService();

    @Test
    public void noSuchUserFoundTest() {
        User user = new User().userId("testUser");
        Exception noSuchUserId = assertThrows(NoSuchUserIdException.class, () -> nullUserService.getUser(user.getUserId()), "NullUserService should throw a NoSuchUserIdException");
        assertEquals("No userId matching UserId[id='testUser'] found in cache", noSuchUserId.getMessage(), "The Exception message should return the id of testUser");
    }

    @Test
    void groupedAddUserAssertions() {
        User user = new User().userId("testUser");
        User actual = nullUserService.addUser(user);
        Set<String> expectedRoles = Collections.emptySet();
        assertAll("userID",
                () -> assertEquals(user, actual, "User should match actual"),
                () -> assertEquals(expectedRoles, actual.getRoles(), "Roles should be empty")
        );
    }

    @Test
    public void groupedDependantAddUserAssertion() {
        assertAll("properties",
                () -> {
                    User user = new User().userId("testUser");
                    User actual = nullUserService.addUser(user);
                    Set<String> expectedRoles = Collections.emptySet();
                    assertNotNull(actual, "created user should not be null");

                    // Executed only if the previous assertion is valid.
                    assertAll("userID",
                            () -> assertEquals(user, actual, "User should match actual"),
                            () -> assertEquals(expectedRoles, actual.getRoles(), "Roles should be empty")
                    );
                },
                () -> {
                    // Grouped assertion, so processed independently
                    // of results of first name assertions.
                    User user = new User().userId("newUser");
                    User actual = nullUserService.addUser(user);
                    Set<String> expectedAuths = Collections.emptySet();
                    assertNotNull(actual, "created user should not be null");

                    // Executed only if the previous assertion is valid.
                    assertAll("userID",
                            () -> assertEquals(user, actual, "User should match actual"),
                            () -> assertEquals(expectedAuths, actual.getAuths(), "Auths should be empty")
                    );
                }
        );
    }
}