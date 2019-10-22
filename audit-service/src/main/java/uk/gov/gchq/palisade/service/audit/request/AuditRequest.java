package uk.gov.gchq.palisade.service.audit.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.gov.gchq.palisade.RequestId;
import uk.gov.gchq.palisade.ToStringBuilder;
import uk.gov.gchq.palisade.service.request.Request;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * This is the abstract class that is passed to the audit-service
 * to be able to store an audit record. The default information is
 * when was the audit record created and by what server.
 * <p>
 * The four immutable data subclasses below can be instantiated by static
 * {@code create(RequestId orig)} factory methods which chain construction by fluid interface definitions.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegisterRequestCompleteAuditRequest.class),
        @JsonSubTypes.Type(value = RegisterRequestExceptionAuditRequest.class),
        @JsonSubTypes.Type(value = ReadRequestCompleteAuditRequest.class),
        @JsonSubTypes.Type(value = ReadRequestExceptionAuditRequest.class)
})
public class AuditRequest<ZoneDateTime> extends Request {

    public final ZonedDateTime timestamp;
    public final String serverIp;
    public final String serverHostname;

    protected AuditRequest() {
        this.timestamp = null;
        this.serverIp = null;
        this.serverHostname = null;
    }

    protected AuditRequest(final RequestId originalRequestId) {
        super.setOriginalRequestId(requireNonNull(originalRequestId));

        this.timestamp = ZonedDateTime.now();
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        serverHostname = inetAddress.getHostName();
        serverIp = inetAddress.getHostAddress();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final AuditRequest<?> that = (AuditRequest<?>) o;
        return Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(serverIp, that.serverIp) &&
                Objects.equals(serverHostname, that.serverHostname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timestamp, serverIp, serverHostname);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("timestamp", timestamp)
                .append("serverIp", serverIp)
                .append("serverHostname", serverHostname)
                .toString();
    }
}
