package uk.gov.gchq.palisade.audit.service.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.gchq.palisade.Context;
import uk.gov.gchq.palisade.RequestId;
import uk.gov.gchq.palisade.User;
import uk.gov.gchq.palisade.resource.impl.DirectoryResource;
import uk.gov.gchq.palisade.resource.impl.FileResource;
import uk.gov.gchq.palisade.resource.impl.SystemResource;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(JUnit4.class)
public class RegisterRequestCompleteAuditRequestTest {
    public final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void RegisterRequestCompleteAuditRequestTest() {
        final RegisterRequestCompleteAuditRequest subject = RegisterRequestCompleteAuditRequest.create(new RequestId().id("456"))
                .withUser(new User().userId("a user"))
                .withLeafResources(Stream.of(new FileResource()).collect(toSet()))
                .withContext(new Context(Stream.of(new AbstractMap.SimpleImmutableEntry<String, Class<?>>("a string", String.class)).collect(toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue))));

        assertThat("RegisterRequestCompleteAuditRequest not constructed", subject.user.getUserId().getId(), is(equalTo("a user")));
    }

    @Test
    public void RegisterRequestCompleteAuditRequestToJsonTest() throws IOException {
        final RegisterRequestCompleteAuditRequest subject = RegisterRequestCompleteAuditRequest.create(new RequestId().id("123"))
                .withUser(new User().userId("user"))
                .withLeafResources(Stream.of(new FileResource().id("/usr/share/resource/test_resource").type("standard").serialisedFormat("none").parent(new DirectoryResource().id("resource").parent(new SystemResource().id("share")))).collect(toSet()))
                .withContext(new Context(Stream.of(new AbstractMap.SimpleImmutableEntry<String, Class<?>>("a string", String.class)).collect(toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue))));

        final JsonNode asNode = this.mapper.readTree(this.mapper.writeValueAsString(subject));
        final Iterable<String> iterable = asNode::fieldNames;

        assertThat("RegisterRequestCompleteAuditRequest not parsed to json", StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.joining(", ")), is(equalTo("class, id, originalRequestId, user, leafResources, context, timestamp, serverIp, serverHostname")));
    }

    @Test
    public void RegisterRequestCompleteAuditRequestFromJsonTest() throws IOException {
        final RegisterRequestCompleteAuditRequest subject = RegisterRequestCompleteAuditRequest.create(new RequestId().id("123"))
                .withUser(new User().userId("user"))
                .withLeafResources(Stream.of(new FileResource().id("/usr/share/resource/test_resource").type("standard").serialisedFormat("none").parent(new DirectoryResource().id("resource").parent(new SystemResource().id("share")))).collect(toSet()))
                .withContext(new Context(Stream.of(new AbstractMap.SimpleImmutableEntry<String, Class<?>>("a string", String.class)).collect(toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue))));

        final String jsonString = "{\"class\":\"RegisterRequestCompleteAuditRequest\",\"id\":{\"id\":\"5942c37d-43e3-419c-bf1c-bb7153d395c8\"},\"originalRequestId\":{\"id\":\"123\"},\"user\":{\"userId\":{\"id\":\"user\"},\"roles\":[],\"auths\":[],\"class\":\"uk.gov.gchq.palisade.User\"},\"leafResources\":[{\"class\":\"uk.gov.gchq.palisade.resource.impl.FileResource\",\"id\":\"/usr/share/resource/test_resource\",\"attributes\":{},\"parent\":{\"class\":\"uk.gov.gchq.palisade.resource.impl.DirectoryResource\",\"id\":\"resource\",\"parent\":{\"class\":\"uk.gov.gchq.palisade.resource.impl.SystemResource\",\"id\":\"share\"}},\"serialisedFormat\":\"none\",\"type\":\"standard\"}],\"context\":{\"class\":\"uk.gov.gchq.palisade.Context\",\"contents\":{\"a string\":\"java.lang.String\"}}}";

        final RegisterRequestCompleteAuditRequest result = this.mapper.readValue(jsonString, RegisterRequestCompleteAuditRequest.class);

        assertThat("RegisterRequestCompleteAuditRequest could not be parsed from json string", subject.context.getContents().keySet().stream().findFirst().orElse("notFound"), is(equalTo("a string")));
    }
}