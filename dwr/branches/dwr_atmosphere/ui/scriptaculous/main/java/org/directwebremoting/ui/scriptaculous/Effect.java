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
package org.directwebremoting.ui.scriptaculous;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;

/**
 * A server side proxy the the Script.aculo.us Effect class
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Effect
{
    /**
     * Call the script.aculo.us <code>Effect.fade()</code> function.
     * @param elementId The element to effect
     */
    public static void fade(String elementId)
    {
        callEffect(elementId, "Fade");
    }

    /**
     * Call the script.aculo.us <code>Effect.appear()</code> function.
     * @param elementId The element to effect
     */
    public static void appear(String elementId)
    {
        callEffect(elementId, "Appear");
    }

    /**
     * Call the script.aculo.us <code>Effect.puff()</code> function.
     * @param elementId The element to effect
     */
    public static void puff(String elementId)
    {
        callEffect(elementId, "Puff");
    }

    /**
     * Call the script.aculo.us <code>Effect.blindUp()</code> function.
     * @param elementId The element to effect
     */
    public static void blindUp(String elementId)
    {
        callEffect(elementId, "BlindUp");
    }

    /**
     * Call the script.aculo.us <code>Effect.blindDown()</code> function.
     * @param elementId The element to effect
     */
    public static void blindDown(String elementId)
    {
        callEffect(elementId, "BlindDown");
    }

    /**
     * Call the script.aculo.us <code>Effect.switchOff()</code> function.
     * @param elementId The element to effect
     */
    public static void switchOff(String elementId)
    {
        callEffect(elementId, "SwitchOff");
    }

    /**
     * Call the script.aculo.us <code>Effect.dropOut()</code> function.
     * @param elementId The element to effect
     */
    public static void dropOut(String elementId)
    {
        callEffect(elementId, "DropOut");
    }

    /**
     * Call the script.aculo.us <code>Effect.shake()</code> function.
     * @param elementId The element to effect
     */
    public static void shake(String elementId)
    {
        callEffect(elementId, "Shake");
    }

    /**
     * Call the script.aculo.us <code>Effect.slideDown()</code> function.
     * @param elementId The element to effect
     */
    public static void slideDown(String elementId)
    {
        callEffect(elementId, "SlideDown");
    }

    /**
     * Call the script.aculo.us <code>Effect.slideUp()</code> function.
     * @param elementId The element to effect
     */
    public static void slideUp(String elementId)
    {
        callEffect(elementId, "SlideUp");
    }

    /**
     * Call the script.aculo.us <code>Effect.squish()</code> function.
     * @param elementId The element to effect
     */
    public static void squish(String elementId)
    {
        callEffect(elementId, "Squish");
    }

    /**
     * Call the script.aculo.us <code>Effect.grow()</code> function.
     * @param elementId The element to effect
     */
    public static void grow(String elementId)
    {
        callEffect(elementId, "Grow");
    }

    /**
     * Call the script.aculo.us <code>Effect.shrink()</code> function.
     * @param elementId The element to effect
     */
    public static void shrink(String elementId)
    {
        callEffect(elementId, "Shrink");
    }

    /**
     * Call the script.aculo.us <code>Effect.pulsate()</code> function.
     * @param elementId The element to effect
     */
    public static void pulsate(String elementId)
    {
        callEffect(elementId, "Pulsate");
    }

    /**
     * Call the script.aculo.us <code>Effect.fold()</code> function.
     * @param elementId The element to effect
     */
    public static void fold(String elementId)
    {
        callEffect(elementId, "Fold");
    }

    /**
     * Call the script.aculo.us <code>Effect.Highlight()</code> function.
     * @param elementId The element to effect
     */
    public static void highlight(String elementId)
    {
        callEffect(elementId, "Highlight");
    }

    /**
     * Call the script.aculo.us <code>Effect.Highlight()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the fade effect, as specified at http://script.aculo.us/
     */
    public static void highlight(String elementId, String options)
    {
        callEffect(elementId, "Highlight", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Fade()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the fade effect, as specified at http://script.aculo.us/
     */
    public static void fade(String elementId, String options)
    {
        callEffect(elementId, "Fade", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Appear()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the appear effect, as specified at http://script.aculo.us/
     */
    public static void appear(String elementId, String options)
    {
        callEffect(elementId, "Appear", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Puff()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the puff effect, as specified at http://script.aculo.us/
     */
    public static void puff(String elementId, String options)
    {
        callEffect(elementId, "Puff", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.BlindUp()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the blindup effect, as specified at http://script.aculo.us/
     */
    public static void blindUp(String elementId, String options)
    {
        callEffect(elementId, "BlindUp", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.BlindDown()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the blinddown effect, as specified at http://script.aculo.us/
     */
    public static void blindDown(String elementId, String options)
    {
        callEffect(elementId, "BlindDown", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.SwitchOff()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the switchf effect, as specified at http://script.aculo.us/
     */
    public static void switchOff(String elementId, String options)
    {
        callEffect(elementId, "SwitchOff", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.DropOut()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the dropout effect, as specified at http://script.aculo.us/
     */
    public static void dropOut(String elementId, String options)
    {
        callEffect(elementId, "DropOut", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Shake()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the shake effect, as specified at http://script.aculo.us/
     */
    public static void shake(String elementId, String options)
    {
        callEffect(elementId, "Shake", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.SlideDown()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the slidedown effect, as specified at http://script.aculo.us/
     */
    public static void slideDown(String elementId, String options)
    {
        callEffect(elementId, "SlideDown", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.SlideUp()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the slideup effect, as specified at http://script.aculo.us/
     */
    public static void slideUp(String elementId, String options)
    {
        callEffect(elementId, "SlideUp", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Squish()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the squish effect, as specified at http://script.aculo.us/
     */
    public static void squish(String elementId, String options)
    {
        callEffect(elementId, "Squish", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Grow()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the grow effect, as specified at http://script.aculo.us/
     */
    public static void grow(String elementId, String options)
    {
        callEffect(elementId, "Grow", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Shrink()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the shrink effect, as specified at http://script.aculo.us/
     */
    public static void shrink(String elementId, String options)
    {
        callEffect(elementId, "Shrink", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Pulsate()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the pulsate effect, as specified at http://script.aculo.us/
     */
    public static void pulsate(String elementId, String options)
    {
        callEffect(elementId, "Pulsate", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Fold()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the fold effect, as specified at http://script.aculo.us/
     */
    public static void fold(String elementId, String options)
    {
        callEffect(elementId, "Fold", options);
    }

    /**
     * @param elementId The element to affect
     * @param function The script.aculo.us effect to employ
     */
    private static void callEffect(String elementId, String function)
    {
        callEffect(elementId, function, null);
    }

	/**
     * @param elementId The element to affect
     * @param function The script.aculo.us effect to employ
     * @param options A string containing options to pass to the script.aculo.us effect, as specified at http://script.aculo.us/
     */
    private static void callEffect(String elementId, String function, String options)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("new Effect.").appendScript(function).appendScript("('").appendScript(elementId).appendScript("'");
        if (options != null && options.length() > 0)
        {
            script.appendScript(", ").appendScript(options);
        }
        script.appendScript(");");
        ScriptSessions.addScript(script);
    }
}
