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

import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * StrutsCreator
 * @author Ariel O. Falduto
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StrutsCreator extends AbstractCreator implements Creator
{
    /**
     * Struts formBean to be retrived
     * @param formBean Struts bean form related.
     */
    public void setFormBean(String formBean)
    {
        this.formBean = formBean;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getType()
     */
    public Class getType()
    {
        synchronized (this)
        {
            if (moduleConfig == null)
            {
                WebContext wc = WebContextFactory.get();
                moduleConfig = RequestUtils.getModuleConfig(wc.getHttpServletRequest(), wc.getServletContext());
                // moduleConfig = ModuleUtils.getInstance().getModuleConfig("", wc.getServletContext()); //$NON-NLS-1$

                // String moduleName = ModuleUtils.getInstance().getModuleName("/", wc.getServletContext());
                // moduleConfig = ModuleUtils.getInstance().getModuleConfig(moduleName, wc.getServletContext());
            }
        }

        try
        {
            return Class.forName(moduleConfig.findFormBeanConfig(formBean).getType());
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.ClassNotFound", moduleConfig.findFormBeanConfig(formBean).getType())); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        // fills for the first time the moduleConfig
        ActionForm formInstance = (ActionForm) WebContextFactory.get().getSession().getAttribute(formBean);
        if (formInstance == null)
        {
            throw new InstantiationException(Messages.getString("Creator.IllegalAccess")); //$NON-NLS-1$
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
}
