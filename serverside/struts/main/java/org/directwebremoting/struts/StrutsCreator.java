package org.directwebremoting.struts;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.FakeHttpServletRequestFactory;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

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
            Class<?> moduleUtilsClass = LocalUtil.classForName("org.apache.struts.util.ModuleUtils");
            getInstanceMethod = moduleUtilsClass.getMethod("getInstance");
            getModuleNameMethod = moduleUtilsClass.getMethod("getModuleName", String.class, ServletContext.class);
            getModuleConfigMethod = moduleUtilsClass.getMethod("getModuleConfig", String.class, ServletContext.class);

            Loggers.STARTUP.debug("Using Struts 1.2 based ModuleUtils code");
        }
        catch (Exception ex)
        {
            getInstanceMethod = null;
            getModuleNameMethod = null;
            getModuleConfigMethod = null;

            Loggers.STARTUP.debug("Failed to find Struts 1.2 ModuleUtils code. Falling back to 1.1 based code");
        }
    }

    /**
     * Struts formBean to be retrieved
     * @param formBean Struts bean form related.
     */
    public void setFormBean(String formBean)
    {
        this.formBean = formBean;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class<?> getType()
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
                        Object utils = getInstanceMethod.invoke(null);

                        // String moduleName = utils.getModuleName("/", wc.getServletContext());
                        String moduleName = (String) getModuleNameMethod.invoke(utils, "/", wc.getServletContext());

                        // moduleConfig = utils.getModuleConfig(moduleName, wc.getServletContext());
                        moduleConfig = (ModuleConfig) getModuleConfigMethod.invoke(utils, moduleName, wc.getServletContext());
                    }
                    catch (Exception ex)
                    {
                        throw new IllegalArgumentException(ex);
                    }
                }
                else
                {
                    HttpServletRequest request = wc.getHttpServletRequest();
                    if (request == null)
                    {
                        Loggers.STARTUP.warn("Using a FakeHttpServletRequest as part of setup");
                        request = FakeHttpServletRequestFactory.create();
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
            throw new IllegalArgumentException("Class not found: " + moduleConfig.findFormBeanConfig(formBean).getType(), ex);
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
            throw new InstantiationException("Can't find formInstance  for " + formBean);
        }

        return formInstance;
    }

    /**
     * The FormBean that we lookup to call methods on
     */
    private String formBean = null;

    /**
     * moduleConfig allows us to do the lookup
     */
    private ModuleConfig moduleConfig = null;

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
}
