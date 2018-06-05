package org.directwebremoting.extend;

/**
 * Reply is a read-only POJO to encapsulate the information required to make a
 * single java call, including the result of the call (either returned data or
 * exception).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Reply
{
    /**
     * Constructor for the success case.
     * @param callId The call callId, copied from the Call object
     * @param reply The successful reply data
     */
    public Reply(String callId, Object reply)
    {
        this.callId = callId;
        this.reply = reply;
        this.th = null;
    }

    /**
     * Constructor for the error case.
     * Reply <b>must</b> be set to null for this constructor to work. This
     * parameter exists to avoid overloading issues. See Java Puzzlers #46 for
     * an example.
     * @param callId The call callId, copied from the Call object
     * @param reply Must be set to null
     * @param th The exception to record against this call.
     */
    public Reply(String callId, Object reply, Throwable th)
    {
        if (reply != null)
        {
            throw new NullPointerException("'reply' must be null when setting an Exception.");
        }

        this.callId = callId;
        this.reply = null;
        this.th = th;
    }

    /**
     * @return Returns the call callId.
     */
    public String getCallId()
    {
        return callId;
    }

    /**
     * @return Returns the call return value.
     */
    public Object getReply()
    {
        return reply;
    }

    /**
     * @return Returns the Exception
     */
    public Throwable getThrowable()
    {
        return th;
    }

    private final String callId;

    private final Object reply;

    private final Throwable th;
}
