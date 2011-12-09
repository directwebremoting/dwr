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
package org.directwebremoting.struts;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.create.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.FakeHttpServletRequest;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * StrutsCreator
 * @author Ariel O. Falduto
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StrutsCreator extends AbstractCreator implements Creator
{
    /**
     *
     */
    public StrutsCreator()
    {
        try
        {
            moduleUtilsClass = LocalUtil.classForName("org.apache.struts.util.ModuleUtils");
            getInstanceMethod = moduleUtilsClass.getMethod("getInstance", new Class[0]);
            getModuleNameMethod = moduleUtilsClass.getMethod("getModuleName", new Class[] { String.class, ServletContext.class });
            getModuleConfigMethod = moduleUtilsClass.getMethod("getModuleConfig", new Class[] { String.class, ServletContext.class });

            log.debug("Using Struts 1.2 based ModuleUtils code");
        }
        catch (Exception ex)
        {
            moduleUtilsClass = null;
            getInstanceMethod = null;
            getModuleNameMethod = null;
            getModuleConfigMethod = null;

            log.debug("Failed to find Struts 1.2 ModuleUtils code. Falling back to 1.1 based code");
        }
    }

    /**
     * Struts formBean to be retrived
     * @param formBean Struts bean form related.
     */
    public void setFormBean(String formBean)
    {
        this.formBean = formBean;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class getType()
    {
        synchronized (this)
        {
            if (moduleConfig == null)
            {
                WebContext wc = WebContextFactory.get();

                if (getInstanceMethod != null)
                {
                    try
                    {
                        // ModuleUtils utils = ModuleUtils.getInstance();
                        Object utils = getInstanceMethod.invoke(null, new Object[0]);

                        // String moduleName = utils.getModuleName("/", wc.getServletContext());
                        String moduleName = (String) getModuleNameMethod.invoke(utils, new Object[] { "/", wc.getServletContext() });

                        // moduleConfig = utils.getModuleConfig(moduleName, wc.getServletContext());
                        moduleConfig = (ModuleConfig) getModuleConfigMethod.invoke(utils, new Object[] { moduleName, wc.getServletContext() });
                    }
                    catch (Exception ex)
                    {
                        throw new IllegalArgumentException(ex.getMessage());
                    }
                }
                else
                {
                    HttpServletRequest request = wc.getHttpServletRequest();
                    if (request == null)
                    {
                        log.warn("Using a FakeHttpServletRequest as part of setup");
                        request = new FakeHttpServletRequest();
                    }

                    moduleConfig = RequestUtils.getModuleConfig(request, wc.getServletContext());
                }
            }
        }

        try
        {
            return LocalUtil.classForName(moduleConfig.findFormBeanConfig(formBean).getType());
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.ClassNotFound", moduleConfig.findFormBeanConfig(formBean).getType()));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        // fills for the first time the moduleConfig
        ActionForm formInstance = (ActionForm) WebContextFactory.get().getSession().getAttribute(formBean);
        if (formInstance == null)
        {
            throw new InstantiationException(Messages.getString("Creator.IllegalAccess"));
        }

        return formInstance;
    }

    /**
     * The FormBean that we lookup to call methods on
     */
    private String formBean;

    /**
     * moduleConfig allows us to do the lookup
     */
    private ModuleConfig moduleConfig;

    /**
     * Reflection access to 1.2 code for compatibility with 1.1
     */
    private Class moduleUtilsClass;

    /**
     * Reflection access to 1.2 code for compatibility with 1.1
     */
    private Method getInstanceMethod;

    /**
     * Reflection access to 1.2 code for compatibility with 1.1
     */
    private Method getModuleNameMethod;

    /**
     * Reflection access to 1.2 code for compatibility with 1.1
     */
    private Method getModuleConfigMethod;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(StrutsCreator.class);
}
