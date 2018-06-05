package org.directwebremoting.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.servlet.PathConstants;
import org.directwebremoting.util.DomUtil;
import org.directwebremoting.util.EmptyEntityResolver;
import org.directwebremoting.util.LogErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The default implementation of PageNormalizer attempts to read from
 * <code>WEB-INF/web.xml</code> to find a <code>welcome-files</code> element,
 * and uses a default of removing "<code>index.html</code>" and
 * "<code>index.jsp</code>" if this procedure fails.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultPageNormalizer implements PageNormalizer
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.PageNormalizer#normalizePage(java.lang.String)
     */
    public String normalizePage(String unnormalized)
    {
        synchronized (initLock)
        {
            if (welcomeFiles == null)
            {
                if (servletContext != null)
                {
                    welcomeFiles = getWebXmlWelcomeFileList(servletContext);
                }
                else
                {
                    WebContext webContext = WebContextFactory.get();
                    if (webContext == null)
                    {
                        log.warn("Can't find ServletContext to check for <welcome-file-list> in web.xml. Assuming defaults.");
                        log.warn(" - To prevent this message from happening, either call the PageNormalizer from a DWR thread");
                        log.warn(" - Or seed the PageNormalizer with a ServletContext before access from outside a DWR thread");
                    }
                    else
                    {
                        ServletContext threadServletContext = webContext.getServletContext();
                        welcomeFiles = getWebXmlWelcomeFileList(threadServletContext);
                    }
                }
            }

            if (welcomeFiles == null)
            {
                log.debug("Using default welcome file list (index.[jsp|htm[l]])");
                welcomeFiles = getDefaultWelcomeFileList();
            }
        }

        if (unnormalized == null)
        {
            return null;
        }

        String normalized = unnormalized;

        if (!normalizeIncludesQueryString)
        {
            int queryPos = normalized.indexOf('?');
            if (queryPos != -1)
            {
                normalized = normalized.substring(0, queryPos);
            }
        }

        if (!normalizeIncludesSessionID)
        {
            final Pattern p = Pattern.compile(
                "([^;\\?#]+)" // group 1: protocol, host, and path (up to first of ; ? or #)
                + ";[^\\?#]+" // sessionid (up to first of ? or #)
                + "(.*)"); // group 2: remainder (any ? or # segments)
            Matcher m = p.matcher(normalized);
            if (m.matches())
            {
                // Concatenate the parts without sessionid
                normalized = m.group(1) + m.group(2);
            }
        }

        for (String welcomeFile : welcomeFiles)
        {
            if (normalized.endsWith(welcomeFile))
            {
                normalized = normalized.substring(0, normalized.length() - welcomeFile.length());
                break;
            }
        }

        return normalized;
    }

    /**
     * Accessor for the list of components to strip to normalize a filename
     * @param context Our route to reading web.xml
     * @return A list of the welcome files from web.xml or null if none are found there
     */
    protected static List<String> getWebXmlWelcomeFileList(ServletContext context)
    {
        try
        {
            InputStream in = context.getResourceAsStream(PathConstants.RESOURCE_WEB_XML);
            if (in == null)
            {
                log.warn("Missing " + PathConstants.RESOURCE_WEB_XML);
                return null;
            }

            synchronized (DefaultPageNormalizer.class)
            {
                if (buildFactory == null)
                {
                    buildFactory = DocumentBuilderFactory.newInstance();
                    buildFactory.setValidating(false);
                }
            }

            DocumentBuilder builder = buildFactory.newDocumentBuilder();
            builder.setEntityResolver(new EmptyEntityResolver());
            builder.setErrorHandler(new LogErrorHandler());

            InputSource is = new InputSource(in);
            Document doc = builder.parse(is);

            Element webapp = doc.getDocumentElement();
            NodeList welcomeFileListNodes = webapp.getElementsByTagName("welcome-file-list");
            if (welcomeFileListNodes.getLength() == 0)
            {
                log.debug("web.xml contains no <welcome-file-list> element");
                return null;
            }

            List<String> reply = new ArrayList<String>();
            for (int i = 0; i < welcomeFileListNodes.getLength(); i++)
            {
                Element welcomeFileListNode = (Element) welcomeFileListNodes.item(i);
                NodeList welcomeFileNodes = welcomeFileListNode.getElementsByTagName("welcome-file");
                for (int j = 0; j < welcomeFileNodes.getLength(); j++)
                {
                    Element welcomeFileNode = (Element) welcomeFileNodes.item(j);
                    String welcomeFile = DomUtil.getText(welcomeFileNode);
                    reply.add(welcomeFile);

                    log.debug("Adding welcome-file: " + welcomeFile);
                }
            }

            return reply;
        }
        catch (Exception ex)
        {
            log.warn("Failed to calculate welcome files from web.xml.", ex);
            return null;
        }
    }

    /**
     * Use the default list of components to strip to normalize a filename
     * @return A list of the default welcome files
     */
    protected static List<String> getDefaultWelcomeFileList()
    {
        List<String> reply = new ArrayList<String>();
        reply.add("index.html");
        reply.add("index.htm");
        reply.add("index.jsp");
        return reply;
    }

    /**
     * Accessor for the list of components to strip to normalize a filename
     * @param welcomeFiles the welcomeFiles to set
     */
    public void setWelcomeFileList(List<String> welcomeFiles)
    {
        this.welcomeFiles = welcomeFiles;
    }

    /**
     * Accessor for the list of components to strip to normalize a filename
     * @param welcomeFileNames the welcomeFiles to set as a comma or newline
     * separated list.
     */
    public void setWelcomeFiles(String welcomeFileNames)
    {
        StringTokenizer st = new StringTokenizer(welcomeFileNames, "\n,");
        while (st.hasMoreTokens())
        {
            welcomeFiles.add(st.nextToken());
        }
    }

    /**
     * Does the page normalizer include query strings in it's definition of
     * pages?
     * @param normalizeIncludesQueryString The new value
     */
    public void setNormalizeIncludesQueryString(boolean normalizeIncludesQueryString)
    {
        this.normalizeIncludesQueryString = normalizeIncludesQueryString;
    }

    /**
     * Does the page normalizer include the session id if it is being appended to the url in it's definition of
     * pages?
     * @param normalizeIncludesSessionID The new value
     */
    public void setNormalizeIncludesSessionID(boolean normalizeIncludesSessionID)
    {
        this.normalizeIncludesSessionID = normalizeIncludesSessionID;
    }

    /**
     * @param servletContext the servletContext to set
     */
    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    /**
     * We need one of these to do the init process.
     */
    private ServletContext servletContext = null;

    /**
     * Does the page normalizer include query strings in it's definition of
     * pages?
     */
    protected boolean normalizeIncludesQueryString = false;

    /**
     * Does the page normalizer include the session id if it is being appended to the url in it's definition of
     * pages?
     */
    protected boolean normalizeIncludesSessionID = false;

    /**
     * How we create new documents
     */
    private static DocumentBuilderFactory buildFactory = null;

    /**
     * The lock to prevent 2 things from calling init at the same time
     */
    protected static final Object initLock = new Object();

    /**
     * The list of filename components to strip to normalize a filename
     */
    private List<String> welcomeFiles;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultPageNormalizer.class);
}
