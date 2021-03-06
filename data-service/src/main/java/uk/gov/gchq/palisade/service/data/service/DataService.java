/*
 * Copyright 2018 Crown Copyright
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

package uk.gov.gchq.palisade.service.data.service;

import uk.gov.gchq.palisade.data.serialise.Serialiser;
import uk.gov.gchq.palisade.reader.common.DataFlavour;
import uk.gov.gchq.palisade.reader.common.DataReader;
import uk.gov.gchq.palisade.reader.exception.NoCapacityException;
import uk.gov.gchq.palisade.service.Service;
import uk.gov.gchq.palisade.service.data.request.ReadRequest;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The core API for the data service.
 * The responsibility of the data service is to take the read request from the
 * client, request the trusted details about the request from the palisade
 * service (what policies to apply, user details, etc). The data service is then
 * loops over the list of resources passing the list of rules that need to be
 * applied, taken from the palisade service response (DataRequestConfig) and the
 * resource to be read to the {@link DataReader}.
 * The {@link DataReader} will then
 * connect to the resource and apply the rules before streaming the data back to
 * the {@link DataService} which forwards the data back to the client.
 */
public interface DataService extends Service {

    /**
     * This method is used by the client code to request the data from the list of
     * resources or a subset of the resources that the palisade service responded
     * with in the {@link uk.gov.gchq.palisade.service.request.DataRequestResponse}.
     * This method will only work if the client has already registered the data
     * request with the palisade service. If the data service is unable to serve the
     * request due to not having the resources to do so, or if it is currently
     * serving too many requests then it may throw a {@link NoCapacityException}.
     *
     * @param request       The {@link ReadRequest} that came from registering the
     *                      request with the palisade service. The request can be
     *                      altered to contain only a subset of the resources to be
     *                      read by this data service instance.
     * @param out           an {@link OutputStream} to sink the file into
     *                      and then apply appropriate complete/exception auditing
     * @throws IOException  The {@link Exception} thrown
     */
    void read(final ReadRequest request, OutputStream out) throws IOException;

    /**
     * Used to add a new serialiser to the data reader
     *
     * @param dataFlavour   the {@link DataFlavour} to be added
     * @param serialiser    the {@link Serialiser} to be added
     * @return a {@link Boolean} true/false on success/failure
     */
    Boolean addSerialiser(final DataFlavour dataFlavour, final Serialiser<?> serialiser);

}
