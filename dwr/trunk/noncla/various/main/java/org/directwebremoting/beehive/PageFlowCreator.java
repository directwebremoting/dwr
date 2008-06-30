package org.directwebremoting.beehive;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;

/**
 * Page Flow Creator
 * The name Creator is a little misleading in that implies that a PageFlow is
 * being created.  This class merely returns the current PageFlowController from
 * the Request
 * @author Kevin Conaway
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PageFlowCreator extends AbstractCreator implements Creator
{
    /**
     * Test to see what implementations of PageFlow are available.
     * @throws ClassNotFoundException If neither Beehive or Weblogic are around.
     */
    public PageFlowCreator() throws ClassNotFoundException
    {
        try
        {
            bhFlowClass = LocalUtil.classForName("org.apache.beehive.netui.pageflow.PageFlowController");

            Class<?> bhUtilClass = LocalUtil.classForName("org.apache.beehive.netui.pageflow.PageFlowUtils");
            bhGetter = bhUtilClass.getMethod("getCurrentPageFlow", HttpServletRequest.class);
        }
        catch (Exception ex)
        {
            // We're expecting this to fail, and notice below
        }

        try
        {
            wlFlowClass = LocalUtil.classForName("com.bea.wlw.netui.pageflow.PageFlowController");

            Class<?> wlUtilClass = LocalUtil.classForName("com.bea.wlw.netui.pageflow.PageFlowUtils");
            wlGetter = wlUtilClass.getMethod("getCurrentPageFlow", HttpServletRequest.class);
        }
        catch (Exception ex)
        {
            // We're expecting this to fail, and notice below
        }

        if ((bhGetter == null && wlGetter == null) || (bhFlowClass == null && wlFlowClass == null))
        {
            throw new ClassNotFoundException("Beehive/Weblogic jar file not available.");
        }
    }

    /**
     * What do we do if both Weblogic and Beehive are available.
     * The default is to use Beehive, but this allows us to alter that.
     * @param forceWebLogic Do we use Weblogic if both are available.
     */
    public void setForceWebLogic(boolean forceWebLogic)
    {
        if (forceWebLogic)
        {
            bhGetter = null;
            bhFlowClass = null;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        if (getter == null)
        {
            getter = (bhGetter != null) ? bhGetter : wlGetter;
        }

        try
        {
            HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
            return getter.invoke(null, request);
        }
        catch (InvocationTargetException ex)
        {
            throw new InstantiationException(ex.getTargetException().toString());
        }
        catch (Exception ex)
        {
            throw new InstantiationException(ex.toString());
        }
    }

    /**
     * @return The PageFlowController that we are using (Beehive/Weblogic)
     */
    public Class<?> getType()
    {
        if (instanceType == null)
        {
            try
            {
                instanceType = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                log.error("Failed to instansiate object to detect type.", ex);
                return Object.class;
            }
        }

        return instanceType;
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(PageFlowCreator.class);

    private Class<?> instanceType = null;

    private Method getter = null;

    private Method bhGetter = null;

    private Method wlGetter = null;

    private Class<?> bhFlowClass = null;

    private Class<?> wlFlowClass = null;
}
