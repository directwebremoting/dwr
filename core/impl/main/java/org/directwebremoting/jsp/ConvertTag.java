package org.directwebremoting.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ScriptBufferUtil;

/**
 * JSP tag to convert an object into javascript using the dwr converter manager
 * Example usage:
 * &lt;%@ taglib uri="http://directwebremoting.org/dwr/" prefix="dwr" %&gt;
 * &lt;script%&gt;
 *    var clientsideObject = &lt;dwr:convert value="${myServersideObject}" /%&gt;;
 * &lt;/script%&gt;
 *
 * @author Lance Semmens <uklance at gmail dot com>
 */
public class ConvertTag extends TagSupport
{
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException
    {
        try
        {
            Container container = ServerContextFactory.get().getContainer();
            ConverterManager converterManager = container.getBean(ConverterManager.class);

            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
            WebContextFactory.WebContextBuilder contextBuilder = container.getBean(WebContextFactory.WebContextBuilder.class);
            contextBuilder.engageThread(container, request, response);

            try
            {
                if (json)
                {
                    ScriptBuffer buffy = new ScriptBuffer();
                    buffy.appendData(value);
                    pageContext.getOut().write(ScriptBufferUtil.createOutput(buffy, converterManager, true));
                }
                else
                {
                    ScriptBuffer buffy = new ScriptBuffer();
                    buffy.appendScript("return ").appendData(value).appendScript(";");

                    // wrap in a closure so that we don't pollute the javascript namespace with loads of variables
                    pageContext.getOut().write(
                        "(function() { " +
                            ScriptBufferUtil.createOutput(buffy, converterManager, false) +
                        " })();"
                    );
                }
            }
            finally
            {
                // clean up the thread local
                contextBuilder.disengageThread();
            }
            return super.doEndTag();
        }
        catch (Exception ex)
        {
            throw new JspException(ex);
        }
    }

    /**
     * @param value the value to be converted
     */
    public void setValue(Object value)
    {
        this.value = value;
    }

    /**
     * If json is false, the normal dwr javascript protocol is used.
     * If json is true, output will be a json response
     * @param json flag for proper json output
     */
    public void setJson(boolean json)
    {
        this.json = json;
    }

    /**
     * The value to convert
     */
    private Object value;

    /**
     * Should the output be proper json?
     */
    private boolean json;
}
