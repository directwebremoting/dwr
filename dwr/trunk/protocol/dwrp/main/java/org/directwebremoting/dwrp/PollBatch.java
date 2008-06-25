/*
 * Copyright 2005 Joe Walker
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
package org.directwebremoting.dwrp;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.ServerException;

/**
 * A container for the information passed in by an Poll request
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollBatch extends Batch
{
    /**
     * @param request The request to parse to fill out this batch
     * @throws ServerException
     */
    public PollBatch(HttpServletRequest request) throws ServerException
    {
        super(request);

        debug = request.getHeader("User-Agent");
        batchId = extractParameter(ProtocolConstants.INBOUND_KEY_BATCHID);
        String prString = extractParameter(ProtocolConstants.INBOUND_KEY_PARTIAL_RESPONSE);
        partialResponse = PartialResponse.fromOrdinal(prString);
    }

    /**
     * @return the batchId
     */
    public String getBatchId()
    {
        return batchId;
    }

    /**
     * The ID of this batch from the browser
     */
    private final String batchId;

    /**
     * @return the partialResponse
     */
    public PartialResponse getPartialResponse()
    {
        return partialResponse;
    }

    /**
     * Does the browser want partial responses?
     */
    private final PartialResponse partialResponse;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "PollBatch[partResp=" + partialResponse + ",debug=" + debug + "]";
    }

    /**
     * A quick string for debug purposes
     */
    private final String debug;
}
