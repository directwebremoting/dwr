package org.directwebremoting.dwrp;

import java.io.PrintWriter;

import org.directwebremoting.extend.ScriptConduit;

/**
 * A ScriptConduit that works with the parent Marshaller.
 * In some ways this is nasty because it has access to essentially private parts
 * of BasePollHandler, however there is nowhere sensible to store them
 * within that class, so this is a hacky simplification.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseScriptConduit implements ScriptConduit
{
    /**
     * Simple ctor
     */
    public BaseScriptConduit(PrintWriter out, String instanceId)
    {
        this.out = out;
        this.instanceId = instanceId;
    }

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    public void setAccessLogLevel(String accessLogLevel)
    {
        this.accessLogLevel = accessLogLevel;
    }

    /**
     * Do we debug all the scripts that we output?
     * @param debugScriptOutput true to debug all of the output scripts (verbose)
     */
    public void setDebugScriptOutput(boolean debugScriptOutput)
    {
        this.debugScriptOutput = debugScriptOutput;
    }

    /**
     * Do we debug all the scripts that we output?
     */
    protected boolean debugScriptOutput = false;

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    protected String accessLogLevel = null;

    /**
     * The response output stream
     */
    protected final PrintWriter out;

    /**
     * What is the ID of the DWR instance that we are responding to?
     */
    protected final String instanceId;
}
