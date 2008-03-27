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
package org.directwebremoting.proxy.scriptaculous;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.dwrutil.DwrUtil;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Effect extends ScriptProxy
{
    /**
     * Http thread constructor, that affects no browsers.
     * Calls to {@link DwrUtil#addScriptSession(ScriptSession)} or to
     * {@link DwrUtil#addScriptSessions(Collection)} will be needed  
     */
    public Effect()
    {
        super();
    }

    /**
     * Non-http thread constructor, that affects no browsers.
     * Calls to {@link Effect#addScriptSession(ScriptSession)} or to
     * {@link Effect#addScriptSessions(Collection)} will be needed
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public Effect(ServletContext sctx)
    {
        super(sctx);
    }

    /**
     * Http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     */
    public Effect(ScriptSession scriptSession)
    {
        super(scriptSession);
    }

    /**
     * Non-http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public Effect(ScriptSession scriptSession, ServletContext sctx)
    {
        super(scriptSession, sctx);
    }

    /**
     * Http thread constructor that alters a number of browsers
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     */
    public Effect(Collection scriptSessions)
    {
        super(scriptSessions);
    }

    /**
     * Non-http thread constructor that alters a number of browsers
     * @param scriptSessions The browsers to alter
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public Effect(Collection scriptSessions, ServletContext sctx)
    {
        super(scriptSessions, sctx);
    }

    /**
     * Call the script.aculo.us <code>Effect.fade()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void fade(String elementId) throws MarshallException
    {
        callEffect(elementId, "fade"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.appear()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void appear(String elementId) throws MarshallException
    {
        callEffect(elementId, "appear"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.puff()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void puff(String elementId) throws MarshallException
    {
        callEffect(elementId, "puff"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.blindUp()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void blindUp(String elementId) throws MarshallException
    {
        callEffect(elementId, "blindUp"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.blindDown()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void blindDown(String elementId) throws MarshallException
    {
        callEffect(elementId, "blindDown"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.switchOff()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void switchOff(String elementId) throws MarshallException
    {
        callEffect(elementId, "switchOff"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.dropOut()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void dropOut(String elementId) throws MarshallException
    {
        callEffect(elementId, "dropOut"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.shake()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void shake(String elementId) throws MarshallException
    {
        callEffect(elementId, "shake"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.slideDown()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void slideDown(String elementId) throws MarshallException
    {
        callEffect(elementId, "slideDown"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.slideUp()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void slideUp(String elementId) throws MarshallException
    {
        callEffect(elementId, "slideUp"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.squish()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void squish(String elementId) throws MarshallException
    {
        callEffect(elementId, "squish"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.grow()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void grow(String elementId) throws MarshallException
    {
        callEffect(elementId, "grow"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.shrink()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void shrink(String elementId) throws MarshallException
    {
        callEffect(elementId, "shrink"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.pulsate()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void pulsate(String elementId) throws MarshallException
    {
        callEffect(elementId, "pulsate"); //$NON-NLS-1$
    }

    /**
     * Call the script.aculo.us <code>Effect.fold()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void fold(String elementId) throws MarshallException
    {
        callEffect(elementId, "fold"); //$NON-NLS-1$
    }

    /**
     * @param elementId
     * @param function
     * @throws MarshallException
     */
    private void callEffect(String elementId, String function) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append("Effect.") //$NON-NLS-1$
            .append(function)
            .append("(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }
}
