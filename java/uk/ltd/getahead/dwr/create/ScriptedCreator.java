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
package uk.ltd.getahead.dwr.create;

import java.io.File;
import java.io.RandomAccessFile;

import javax.servlet.ServletContext;

import org.apache.bsf.BSFManager;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.WebContextFactory;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A creator that uses BeanShell to evaluate some script to create an object.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Dennis [devel at muhlesteins dot com]
 */
public class ScriptedCreator extends AbstractCreator implements Creator
{
    /**
     * The language that we are scripting in. Passed to BSF.
     * @return Returns the language.
     */
    public String getLanguage()
    {
        return language;
    }

    /**
     * @param language The language to set.
     */
    public void setLanguage(String language)
    {
        // It would be good to be ablt to check this, but this seems to fail
        // almost all the time
        // if (BSFManager.isLanguageRegistered(language))
        // {
        //     throw new IllegalArgumentException(Messages.getString("ScriptedCreator.IllegalLanguage", language)); //$NON-NLS-1$
        // }
        this.language = language;
    }

    /**
     * Are we caching the script (default: false)
     * @return Returns the reloadable variable
     */
    public String isCacheable()
    {
        return String.valueOf(cacheable);
    }

    /**
     * @param cacheable Whether or not to reload the script.  
     * The default is <b>true</b>. This parameter is only used if scriptPath is
     * used instead of script.  When reloadable is true, ScriptedCreator will
     * check to see if the script has been modified before returning the
     * existing created class.
     */
    public void setCacheable(String cacheable)
    {
        this.cacheable = Boolean.valueOf(cacheable).booleanValue();
    }

    /**
     * @return Returns the path of the script.
     */
    public String getScriptPath()
    {
        return scriptPath;
    }

    /**
     * @param scriptPath Context reletive path to script.
     */
    public void setScriptPath(String scriptPath)
    {
        if (scriptSrc != null)
        {
            throw new IllegalArgumentException(Messages.getString("ScriptCreator.MultipleScript")); //$NON-NLS-1$
        }

        this.scriptPath = scriptPath;
    }

	/**
     * @return Whether or not the script (located at scriptPath) has been modified.
     */
    private boolean scriptUpdated()
    {
        if (null == scriptPath)
        {
            return false;
        }

        ServletContext sc = WebContextFactory.get().getServletContext();
        File scriptFile = new File(sc.getRealPath(scriptPath));
        if (scriptModified < scriptFile.lastModified())
        {
            log.debug("Script has been updated."); //$NON-NLS-1$
            clazz = null; // make sure that this gets re-compiled.
            return true;
        }

        return false;
    }

    /**
     * @return Returns the script.
     * @throws InstantiationException 
     */
    public String getScript() throws InstantiationException
    {
        if (scriptSrc != null)
        {
            return scriptSrc;
        }

        if (scriptPath == null)
        {
            throw new InstantiationException(Messages.getString("ScriptedCreator.MissingScript")); //$NON-NLS-1$
        }

        if (cachedScript != null && (!cacheable || !scriptUpdated()) )
        {
            return cachedScript;
        }

        try
        {
            // load the script from the path
            log.debug("Loading Script from Path: " + scriptPath); //$NON-NLS-1$
            RandomAccessFile in = null;

            try
            {
                ServletContext sc = WebContextFactory.get().getServletContext();
                File scriptFile = new File(sc.getRealPath(scriptPath));
                
                scriptModified = scriptFile.lastModified();
                in = new RandomAccessFile(scriptFile, "r"); //$NON-NLS-1$
                byte bytes[] = new byte[(int) in.length()];
                in.readFully(bytes);
                cachedScript = new String(bytes);

                return cachedScript;
            }
            finally
            {
                if (null != in)
                {
                    in.close();
                }
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
            throw new InstantiationException(Messages.getString("ScriptCreator.MissingScript")); //$NON-NLS-1$
        }
    }

    /**
     * @param scriptSrc The script to set.
     */
    public void setScript(String scriptSrc)
    {
        if (scriptPath != null)
        {
            throw new IllegalArgumentException(Messages.getString("ScriptCreator.MultipleScript")); //$NON-NLS-1$
        }

        if (scriptSrc == null || scriptSrc.trim().length() == 0)
        {
            throw new IllegalArgumentException(Messages.getString("ScriptedCreator.MissingScript")); //$NON-NLS-1$
        }

        this.scriptSrc = scriptSrc;
    }

    /**
     * What sort of class do we create?
     * @param classname The name of the class
     */
    public void setClass(String classname)
    {
        try
        {
            this.clazz = Class.forName(classname);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.ClassNotFound", classname)); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getType()
     */
    public Class getType()
    {
        if (clazz == null || (!cacheable && scriptUpdated()))
        {
            try
            {
                clazz = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                log.error("Failed to instansiate object to detect type.", ex); //$NON-NLS-1$
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
			if (clazz != null)
            {
                return clazz.newInstance();
            }

            BSFManager bsfman = new BSFManager();
            return bsfman.eval(language, (null == scriptPath ? "dwr.xml" : scriptPath), 0, 0, getScript()); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            log.error("Error executing script", ex); //$NON-NLS-1$
            throw new InstantiationException(Messages.getString("Creator.IllegalAccess")); //$NON-NLS-1$
        }
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ScriptedCreator.class);

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
    private String scriptSrc = null;

    /**
     * The path of the script we are asking BSF to execute.
     */
    private String scriptPath = null;

    /**
     * Whether or not to reload the script.  Only used if scriptPath is used.
     * ie: An inline script is not reloadable
     */
    private boolean cacheable = true;

    /**
     * Script modified time. Only used when scriptPath is used.
     */
    private long scriptModified = -1;

    /**
     * Contents of script loaded from scriptPath
     */
    private String cachedScript;
}
