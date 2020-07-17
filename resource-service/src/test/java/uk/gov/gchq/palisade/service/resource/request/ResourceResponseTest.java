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
package uk.gov.gchq.palisade.service.resource.request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;

import uk.gov.gchq.palisade.Context;
import uk.gov.gchq.palisade.User;
import uk.gov.gchq.palisade.resource.LeafResource;
import uk.gov.gchq.palisade.resource.impl.FileResource;
import uk.gov.gchq.palisade.resource.impl.SystemResource;
import uk.gov.gchq.palisade.service.SimpleConnectionDetail;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@JsonTest
public class ResourceResponseTest {

    @Autowired
    private JacksonTester<ResourceResponse> jacksonTester;


    /**
     * Create the object using the builder and then serialise it to a Json string. Test the content of the Json string
     *
     * @throws IOException if it fails to parse the object
     */
    @Test
    public void testSerialiseResourceResponseToJson() throws IOException {

        Context context = new Context().purpose("testContext");
        User user = new User().userId("testUserId");
        LeafResource resource = new FileResource().id("/test/file.format")
                .type("java.lang.String")
                .serialisedFormat("format")
                .connectionDetail(new SimpleConnectionDetail().serviceName("test-service"))
                .parent(new SystemResource().id("/test"));
        ResourceResponse resourceResponse = ResourceResponse.Builder.create()
                .withUserId("originalUserID")
                .withResourceId("originalResourceID")
                .withContext(context)
                .withUser(user)
                .withResource(resource);

        JsonContent<ResourceResponse> resourceRequestJsonContent = jacksonTester.write(resourceResponse);

        assertThat(resourceRequestJsonContent).extractingJsonPathStringValue("$.userId").isEqualTo("originalUserID");
        assertThat(resourceRequestJsonContent).extractingJsonPathStringValue("$.resourceId").isEqualTo("originalResourceID");
        assertThat(resourceRequestJsonContent).extractingJsonPathStringValue("$.user.userId.id").isEqualTo("testUserId");
        assertThat(resourceRequestJsonContent).extractingJsonPathStringValue("$.context.contents.purpose").isEqualTo("testContext");
        assertThat(resourceRequestJsonContent).extractingJsonPathStringValue("$.resource.id").isEqualTo("/test/file.format");

    }

    /**
     * Create the ResourceResponse object from a Json string and then test the content of the object.
     *
     * @throws IOException if it fails to parse the string into an object
     */
    @Test
    public void testDeserializeJsonToResourceResponse() throws IOException {

        String jsonString = "{\"userId\":\"originalUserID\",\"resourceId\":\"originalResourceID\",\"context\":{\"class\":\"uk.gov.gchq.palisade.Context\",\"contents\":{\"purpose\":\"testContext\"}},\"user\":{\"userId\":{\"id\":\"testUserId\"},\"roles\":[],\"auths\":[],\"class\":\"uk.gov.gchq.palisade.User\"},\"resource\":{\"class\":\"uk.gov.gchq.palisade.resource.impl.FileResource\",\"id\":\"/test/file.format\",\"attributes\":{},\"connectionDetail\":{\"class\":\"uk.gov.gchq.palisade.service.SimpleConnectionDetail\",\"serviceName\":\"test-service\"},\"parent\":{\"class\":\"uk.gov.gchq.palisade.resource.impl.SystemResource\",\"id\":\"/test/\"},\"serialisedFormat\":\"format\",\"type\":\"java.lang.String\"}}";

        ObjectContent<ResourceResponse> resourceRequestContent = jacksonTester.parse(jsonString);

        ResourceResponse resourceResponse = resourceRequestContent.getObject();
        assertThat(resourceResponse.getUserId()).isEqualTo("originalUserID");
        assertThat(resourceResponse.getResourceId()).isEqualTo("originalResourceID");

        assertThat(resourceResponse.getContext().getPurpose()).isEqualTo("testContext");
        assertThat(resourceResponse.getUser().getUserId().getId()).isEqualTo("testUserId");
        assertThat(resourceResponse.resource.getId()).isEqualTo("/test/file.format");

    }

    /**
     * Create the ResourceResponse object from a ResourceRequest, serialise it and then test the content of the object.
     *
     * @throws IOException if it fails to parse the string into an object
     */
    @Test
    public void testSerialisePolicyResponseUsingResourceRequestToJson() throws IOException {

        Context context = new Context().purpose("testContext");
        User user = new User().userId("testUserId");
        LeafResource resource = new FileResource().id("/test/file.format")
                .type("java.lang.String")
                .serialisedFormat("format")
                .connectionDetail(new SimpleConnectionDetail().serviceName("test-service"))
                .parent(new SystemResource().id("/test"));

        ResourceRequest resourceRequest = ResourceRequest.Builder.create()
                .withUserId("originalUserID")
                .withResourceId("originalResourceID")
                .withContext(context)
                .withUser(user);

        ResourceResponse resourceResponse = ResourceResponse.Builder.create(resourceRequest).withResource(resource);
        JsonContent<ResourceResponse> resourceResponseJsonContent = jacksonTester.write(resourceResponse);

        assertThat(resourceResponseJsonContent).extractingJsonPathStringValue("$.userId").isEqualTo("originalUserID");
        assertThat(resourceResponseJsonContent).extractingJsonPathStringValue("$.resourceId").isEqualTo("originalResourceID");
        assertThat(resourceResponseJsonContent).extractingJsonPathStringValue("$.context.contents.purpose").isEqualTo("testContext");
        assertThat(resourceResponseJsonContent).extractingJsonPathStringValue("$.user.userId.id").isEqualTo("testUserId");
        assertThat(resourceResponseJsonContent).extractingJsonPathStringValue("$.resource.id").isEqualTo("/test/file.format");

    }
}