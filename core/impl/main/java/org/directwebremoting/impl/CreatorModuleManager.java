package org.directwebremoting.impl;

import java.util.Collection;

import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.Module;
import org.directwebremoting.extend.ModuleManager;

/**
 * An adapter ModuleManager for Creators
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public class CreatorModuleManager implements ModuleManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ModuleManager#getModuleNames(boolean)
     */
    public Collection<String> getModuleNames(boolean includeHidden)
    {
        return creatorManager.getCreatorNames(includeHidden);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ModuleManager#getModule(java.lang.String, boolean)
     */
    public Module getModule(String name, boolean includeHidden)
    {
        return new CreatorModule(creatorManager.getCreator(name, includeHidden), ajaxFilterManager, accessControl, allowImpossibleTests, accessLogLevel, debug);
    }

    /**
     * @param creatorManager the creatorManager to set
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
    }

    /**
     * @param ajaxFilterManager the ajaxFilterManager to set
     */
    public void setAjaxFilterManager(AjaxFilterManager ajaxFilterManager)
    {
        this.ajaxFilterManager = ajaxFilterManager;
    }

    /**
     * @param accessControl the accessControl to set
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * @param allowImpossibleTests the allowImpossibleTests to set
     */
    public void setAllowImpossibleTests(boolean allowImpossibleTests)
    {
        this.allowImpossibleTests = allowImpossibleTests;
    }

    /**
     * @param accessLogLevel the accessLogLevel to set
     */
    public void setAccessLogLevel(String accessLogLevel)
    {
        this.accessLogLevel = accessLogLevel;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    private CreatorManager creatorManager;

    private AjaxFilterManager ajaxFilterManager;

    private AccessControl accessControl;

    private boolean allowImpossibleTests;

    private String accessLogLevel;

    private boolean debug;
}

