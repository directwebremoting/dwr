package org.directwebremoting.dwrp;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.Logger;

/**
 * Implementaion of ScriptConduit that just resumes a continuation.
 */
public class ResumeContinuationScriptConduit extends ScriptConduit
{
    /**
     * @param continuation
     */
    public ResumeContinuationScriptConduit(Continuation continuation)
    {
        super(RANK_PROCEDURAL);
        this.continuation = continuation;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
     */
    public boolean addScript(ScriptBuffer script)
    {
        try
        {
            continuation.resume();
        }
        catch (Exception ex)
        {
            log.warn("Exception in continuation.resume()", ex);
        }

        // never actually handle the script!
        return false;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ResumeContinuationScriptConduit.class);

    /**
     * The Jetty continuation
     */
    private final Continuation continuation;
}