package uk.ltd.getahead.dwr;

/**
 * Call encapsulates the information required to make a single java call.
 * This includes the results (either returned data or exception).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Call
{
    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param scriptName The scriptName to set.
     */
    public void setScriptName(String scriptName)
    {
        this.scriptName = scriptName;
    }

    /**
     * @return Returns the scriptName.
     */
    public String getScriptName()
    {
        return scriptName;
    }

    /**
     * @param methodName The methodName to set.
     */
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    /**
     * @return Returns the methodName.
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * @param inctx The inctx to set.
     */
    public void setInboundContext(InboundContext inctx)
    {
        this.inctx = inctx;
    }

    /**
     * @return Returns the inctx.
     */
    public InboundContext getInboundContext()
    {
        return inctx;
    }

    /**
     * @param reply The reply to set.
     */
    public void setReply(OutboundVariable reply)
    {
        this.reply = reply;
    }

    /**
     * @return Returns the reply.
     */
    public OutboundVariable getReply()
    {
        return reply;
    }

    /**
     * @param th The th to set.
     */
    public void setThrowable(Throwable th)
    {
        this.th = th;
    }

    /**
     * @return Returns the th.
     */
    public Throwable getThrowable()
    {
        return th;
    }

    private String id = null;

    private String scriptName = null;

    private String methodName = null;

    private InboundContext inctx = new InboundContext();

    private OutboundVariable reply =  null;

    private Throwable th = null;
}
