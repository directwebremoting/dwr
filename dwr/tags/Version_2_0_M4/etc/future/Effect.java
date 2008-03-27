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

import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Effect extends ScriptProxy
{
    /**
     * Call the script.aculo.us <code>Effect.fade()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void fade(String elementId, Map params) throws MarshallException
    {
        callEffect(elementId, "Fade");
    }

    /**
     * Call the script.aculo.us <code>Effect.appear()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void appear(String elementId) throws MarshallException
    {
        callEffect(elementId, "Appear");
    }

    /**
     * Call the script.aculo.us <code>Effect.puff()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void puff(String elementId) throws MarshallException
    {
        callEffect(elementId, "Puff");
    }

    /**
     * Call the script.aculo.us <code>Effect.blindUp()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void blindUp(String elementId) throws MarshallException
    {
        callEffect(elementId, "BlindUp");
    }

    /**
     * Call the script.aculo.us <code>Effect.blindDown()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void blindDown(String elementId) throws MarshallException
    {
        callEffect(elementId, "BlindDown");
    }

    /**
     * Call the script.aculo.us <code>Effect.switchOff()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void switchOff(String elementId) throws MarshallException
    {
        callEffect(elementId, "SwitchOff");
    }

    /**
     * Call the script.aculo.us <code>Effect.dropOut()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void dropOut(String elementId) throws MarshallException
    {
        callEffect(elementId, "DropOut");
    }

    /**
     * Call the script.aculo.us <code>Effect.shake()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void shake(String elementId) throws MarshallException
    {
        callEffect(elementId, "Shake");
    }

    /**
     * Call the script.aculo.us <code>Effect.slideDown()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void slideDown(String elementId) throws MarshallException
    {
        callEffect(elementId, "SlideDown");
    }

    /**
     * Call the script.aculo.us <code>Effect.slideUp()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void slideUp(String elementId) throws MarshallException
    {
        callEffect(elementId, "SlideUp");
    }

    /**
     * Call the script.aculo.us <code>Effect.squish()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void squish(String elementId) throws MarshallException
    {
        callEffect(elementId, "Squish");
    }

    /**
     * Call the script.aculo.us <code>Effect.grow()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void grow(String elementId) throws MarshallException
    {
        callEffect(elementId, "Grow");
    }

    /**
     * Call the script.aculo.us <code>Effect.shrink()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void shrink(String elementId) throws MarshallException
    {
        callEffect(elementId, "Shrink");
    }

    /**
     * Call the script.aculo.us <code>Effect.pulsate()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void pulsate(String elementId) throws MarshallException
    {
        callEffect(elementId, "Pulsate");
    }

    /**
     * Call the script.aculo.us <code>Effect.fold()</code> function.
     * @param elementId The element to effect
     * @throws MarshallException
     */
    public void fold(String elementId) throws MarshallException
    {
        callEffect(elementId, "Fold");
    }

    /**
     * @param elementId
     * @param function
     * @throws MarshallException
     */
    private void callEffect(String elementId, String function) throws MarshallException
    {
        addScript(getEffect(elementId, function, null));
    }
    
    public String getEffect(String elementId, String function, Map params) {
        OutboundVariable elementIdOv = getWebContext().toJavascript(elementId);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append("new Effect.")
            .append(function)
            .append("(")
            .append(elementIdOv.getAssignCode());
        
            // If the parameter Map is non-empty, construct a
            // a corresponding javascript array and place it
            // it in the Scriptaculous Effect constructor
            if (params != null && params.size() >0) {
                script.append(", {");
                for (Iterator it = params.entrySet().iterator();it.hasNext();) {
                    Map.Entry entry = (Map.Entry)it.next();
                    script.append(entry.getKey());
                    script.append(": '");
                    script.append(entry.getValue());
                    script.append("'");
                    if (it.hasNext()) {
                        script.append(",");
                    } else {
                        script.append("}");
                    }
                }
            }
            script.append(");");     
        return script.toString();
        
    }
 }




