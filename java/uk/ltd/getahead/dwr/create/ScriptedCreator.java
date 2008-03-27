package uk.ltd.getahead.dwr.create;

import java.util.Map;

import org.apache.bsf.BSFManager;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.Log;

/**
 * A creator that simply uses the default constructor each time it is called.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptedCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(java.lang.String, org.w3c.dom.Element)
     */
    public void init(Map params) throws IllegalArgumentException
    {
        this.language = (String) params.get("language"); //$NON-NLS-1$
        // if (BSFManager.isLanguageRegistered(language))
        // {
        //     throw new IllegalArgumentException(Messages.getString("ScriptedCreator.IllegalLanguage", language)); //$NON-NLS-1$
        // }

        this.script = (String) params.get("script"); //$NON-NLS-1$
        if (script == null || script.trim().length() == 0)
        {
            throw new IllegalArgumentException(Messages.getString("ScriptedCreator.MissingScript")); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getType()
     */
    public Class getType()
    {
        if (clazz == null)
        {
            try
            {
                clazz = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                Log.error("Failed to instansiate object to detect type.", ex); //$NON-NLS-1$
                return Object.class;
            }
        }

        return clazz;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        try
        {
            return bsfman.eval(language, "dwr.xml", 0, 0, script); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            Log.error("Error executing script", ex); //$NON-NLS-1$
            throw new InstantiationException(Messages.getString("Creator.IllegalAccess")); //$NON-NLS-1$
        }
    }

    /**
     * Our script evaluation environment
     */
    private BSFManager bsfman = new BSFManager();

    /**
     * The cached type of object that we are creating.
     */
    private Class clazz = null;

    /**
     * The language that we are scripting in. Passed to BSF.
     */
    private String language = null;

    /**
     * The script that we are asking BSF to execute in order to get an object.
     */
    private String script = null;
}
