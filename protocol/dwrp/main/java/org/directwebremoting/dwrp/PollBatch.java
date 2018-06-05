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

        if (getNextReverseAjaxIndex() == null)
        {
            throw new IllegalArgumentException("A reverse ajax poll must specify nextReverseAjaxIndex.");
        }
        debug = request.getHeader("User-Agent");
        String prString = extractParameter(ProtocolConstants.INBOUND_KEY_PARTIAL_RESPONSE, THROW);
        partialResponse = PartialResponse.fromOrdinal(prString);
    }

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
