package uk.ltd.getahead.dwr.create;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.WebContextFactory;
import uk.ltd.getahead.dwr.util.Logger;

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
            bhFlowClass = Class.forName("org.apache.beehive.netui.pageflow.PageFlowController"); //$NON-NLS-1$

            Class bhUtilClass = Class.forName("org.apache.beehive.netui.pageflow.PageFlowUtils"); //$NON-NLS-1$
            bhGetter = bhUtilClass.getMethod("getCurrentPageFlow", new Class[] { HttpServletRequest.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
        }

        try
        {
            wlFlowClass = Class.forName("com.bea.wlw.netui.pageflow.PageFlowController"); //$NON-NLS-1$

            Class wlUtilClass = Class.forName("com.bea.wlw.netui.pageflow.PageFlowUtils"); //$NON-NLS-1$
            wlGetter = wlUtilClass.getMethod("getCurrentPageFlow", new Class[] { HttpServletRequest.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
        }
        
        if ((bhGetter == null && wlGetter == null) ||
           (bhFlowClass == null && wlFlowClass == null))
        {
            throw new ClassNotFoundException("Beehive/Weblogic Creator not available."); //$NON-NLS-1$
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
     * @see uk.ltd.getahead.dwr.Creator#getInstance()
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
            return getter.invoke(null, new Object[] { request });
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
    public Class getType()
    {
        if (instanceType == null)
        {
            try
            {
                instanceType = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                log.error("Failed to instansiate object to detect type.", ex); //$NON-NLS-1$
                return Object.class;
            }
        }

        return instanceType;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(PageFlowCreator.class);

    private Class instanceType;

    private Method getter;
    private Method bhGetter;
    private Method wlGetter;

    private Class bhFlowClass;
    private Class wlFlowClass;
} 
