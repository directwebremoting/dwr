package org.directwebremoting.proxy.scriptaculous;

import java.util.Collection;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * A server side proxy the the Script.aculo.us Effect class
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mitch Gorman
 * @deprecated Use org.directwebremoting.ui.scriptaculous.Effect
 * @see org.directwebremoting.ui.scriptaculous.Effect
 */
@Deprecated
public class Effect extends ScriptProxy
{
    /**
     * Http thread constructor, that affects no browsers.
     * Calls to {@link Effect#addScriptSession(ScriptSession)} or to
     * {@link Effect#addScriptSessions(Collection)} will be needed
     */
    public Effect()
    {
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
     * Http thread constructor that alters a number of browsers
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     */
    public Effect(Collection<ScriptSession> scriptSessions)
    {
        super(scriptSessions);
    }

    /**
     * Call the script.aculo.us <code>Effect.fade()</code> function.
     * @param elementId The element to effect
     */
    public void fade(String elementId)
    {
        callEffect(elementId, "Fade");
    }

    /**
     * Call the script.aculo.us <code>Effect.appear()</code> function.
     * @param elementId The element to effect
     */
    public void appear(String elementId)
    {
        callEffect(elementId, "Appear");
    }

    /**
     * Call the script.aculo.us <code>Effect.puff()</code> function.
     * @param elementId The element to effect
     */
    public void puff(String elementId)
    {
        callEffect(elementId, "Puff");
    }

    /**
     * Call the script.aculo.us <code>Effect.blindUp()</code> function.
     * @param elementId The element to effect
     */
    public void blindUp(String elementId)
    {
        callEffect(elementId, "BlindUp");
    }

    /**
     * Call the script.aculo.us <code>Effect.blindDown()</code> function.
     * @param elementId The element to effect
     */
    public void blindDown(String elementId)
    {
        callEffect(elementId, "BlindDown");
    }

    /**
     * Call the script.aculo.us <code>Effect.switchOff()</code> function.
     * @param elementId The element to effect
     */
    public void switchOff(String elementId)
    {
        callEffect(elementId, "SwitchOff");
    }

    /**
     * Call the script.aculo.us <code>Effect.dropOut()</code> function.
     * @param elementId The element to effect
     */
    public void dropOut(String elementId)
    {
        callEffect(elementId, "DropOut");
    }

    /**
     * Call the script.aculo.us <code>Effect.shake()</code> function.
     * @param elementId The element to effect
     */
    public void shake(String elementId)
    {
        callEffect(elementId, "Shake");
    }

    /**
     * Call the script.aculo.us <code>Effect.slideDown()</code> function.
     * @param elementId The element to effect
     */
    public void slideDown(String elementId)
    {
        callEffect(elementId, "SlideDown");
    }

    /**
     * Call the script.aculo.us <code>Effect.slideUp()</code> function.
     * @param elementId The element to effect
     */
    public void slideUp(String elementId)
    {
        callEffect(elementId, "SlideUp");
    }

    /**
     * Call the script.aculo.us <code>Effect.squish()</code> function.
     * @param elementId The element to effect
     */
    public void squish(String elementId)
    {
        callEffect(elementId, "Squish");
    }

    /**
     * Call the script.aculo.us <code>Effect.grow()</code> function.
     * @param elementId The element to effect
     */
    public void grow(String elementId)
    {
        callEffect(elementId, "Grow");
    }

    /**
     * Call the script.aculo.us <code>Effect.shrink()</code> function.
     * @param elementId The element to effect
     */
    public void shrink(String elementId)
    {
        callEffect(elementId, "Shrink");
    }

    /**
     * Call the script.aculo.us <code>Effect.pulsate()</code> function.
     * @param elementId The element to effect
     */
    public void pulsate(String elementId)
    {
        callEffect(elementId, "Pulsate");
    }

    /**
     * Call the script.aculo.us <code>Effect.fold()</code> function.
     * @param elementId The element to effect
     */
    public void fold(String elementId)
    {
        callEffect(elementId, "Fold");
    }

    /**
     * Call the script.aculo.us <code>Effect.Highlight()</code> function.
     * @param elementId The element to effect
     */
    public void highlight(String elementId)
    {
        callEffect(elementId, "Highlight");
    }

    /**
     * Call the script.aculo.us <code>Effect.Highlight()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the fade effect, as specified at http://script.aculo.us/
     */
    public void highlight(String elementId, String options)
    {
        callEffect(elementId, "Highlight", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Fade()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the fade effect, as specified at http://script.aculo.us/
     */
    public void fade(String elementId, String options)
    {
        callEffect(elementId, "Fade", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Appear()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the appear effect, as specified at http://script.aculo.us/
     */
    public void appear(String elementId, String options)
    {
        callEffect(elementId, "Appear", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Puff()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the puff effect, as specified at http://script.aculo.us/
     */
    public void puff(String elementId, String options)
    {
        callEffect(elementId, "Puff", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.BlindUp()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the blindup effect, as specified at http://script.aculo.us/
     */
    public void blindUp(String elementId, String options)
    {
        callEffect(elementId, "BlindUp", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.BlindDown()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the blinddown effect, as specified at http://script.aculo.us/
     */
    public void blindDown(String elementId, String options)
    {
        callEffect(elementId, "BlindDown", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.SwitchOff()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the switchf effect, as specified at http://script.aculo.us/
     */
    public void switchOff(String elementId, String options)
    {
        callEffect(elementId, "SwitchOff", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.DropOut()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the dropout effect, as specified at http://script.aculo.us/
     */
    public void dropOut(String elementId, String options)
    {
        callEffect(elementId, "DropOut", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Shake()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the shake effect, as specified at http://script.aculo.us/
     */
    public void shake(String elementId, String options)
    {
        callEffect(elementId, "Shake", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.SlideDown()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the slidedown effect, as specified at http://script.aculo.us/
     */
    public void slideDown(String elementId, String options)
    {
        callEffect(elementId, "SlideDown", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.SlideUp()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the slideup effect, as specified at http://script.aculo.us/
     */
    public void slideUp(String elementId, String options)
    {
        callEffect(elementId, "SlideUp", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Squish()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the squish effect, as specified at http://script.aculo.us/
     */
    public void squish(String elementId, String options)
    {
        callEffect(elementId, "Squish", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Grow()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the grow effect, as specified at http://script.aculo.us/
     */
    public void grow(String elementId, String options)
    {
        callEffect(elementId, "Grow", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Shrink()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the shrink effect, as specified at http://script.aculo.us/
     */
    public void shrink(String elementId, String options)
    {
        callEffect(elementId, "Shrink", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Pulsate()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the pulsate effect, as specified at http://script.aculo.us/
     */
    public void pulsate(String elementId, String options)
    {
        callEffect(elementId, "Pulsate", options);
    }

    /**
     * Call the script.aculo.us <code>Effect.Fold()</code> function.
     * @param elementId The element to affect
     * @param options A string containing options to pass to the fold effect, as specified at http://script.aculo.us/
     */
    public void fold(String elementId, String options)
    {
        callEffect(elementId, "Fold", options);
    }

    /**
     * @param elementId The element to affect
     * @param function The script.aculo.us effect to employ
     */
    private void callEffect(String elementId, String function)
    {
        callEffect(elementId, function, null);
    }

	/**
     * @param elementId The element to affect
     * @param function The script.aculo.us effect to employ
     * @param options A string containing options to pass to the script.aculo.us effect, as specified at http://script.aculo.us/
     */
    private void callEffect(String elementId, String function, String options)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("new Effect.").appendScript(function).appendScript("('").appendScript(elementId).appendScript("'");
        if (options != null && options.length() > 0)
        {
            script.appendScript(", ").appendScript(options);
        }
        script.appendScript(");");
        addScript(script);
    }
}
