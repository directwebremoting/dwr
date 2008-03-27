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
package org.directwebremoting.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.servlet.PathConstants;
import org.directwebremoting.util.DomUtil;
import org.directwebremoting.util.EmptyEntityResolver;
import org.directwebremoting.util.LogErrorHandler;
import org.directwebremoting.util.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The default implementation of PageNormalizer attempts to read from
 * <code>WEB-INF/web.xml</code> to find a <code>welcome-files</code> element,
 * and uses a default of removing "<code>index.html</code>" and
 * "<code>index.jsp</code>" if this proceedure fails.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultPageNormalizer implements PageNormalizer
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.PageNormalizer#normalizePage(java.lang.String)
     */
    public String normalizePage(String unnormalized)
    {
        synchronized (welcomeFiles)
        {
            if (!welcomeFilesPopulated)
            {
                initWebXmlWelcomeFileList();
            }

            if (!welcomeFilesPopulated)
            {
                initDefaultWelcomeFileList();
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

        for (Iterator it = welcomeFiles.iterator(); it.hasNext();)
        {
            String welcomeFile = (String) it.next();
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
     */
    protected void initWebXmlWelcomeFileList()
    {
        try
        {
            ServletContext context = WebContextFactory.get().getServletContext();
            InputStream in = context.getResourceAsStream(PathConstants.RESOURCE_WEB_XML);
            if (in == null)
            {
                log.warn("Missing " + PathConstants.RESOURCE_WEB_XML);
                return;
            }

            if (buildFactory == null)
            {
                buildFactory = DocumentBuilderFactory.newInstance();
                buildFactory.setValidating(false);
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
                return;
            }

            for (int i = 0; i < welcomeFileListNodes.getLength(); i++)
            {
                Element welcomeFileListNode = (Element) welcomeFileListNodes.item(i);
                NodeList welcomeFileNodes = welcomeFileListNode.getElementsByTagName("welcome-file");
                for (int j = 0; j < welcomeFileNodes.getLength(); j++)
                {
                    Element welcomeFileNode = (Element) welcomeFileNodes.item(j);
                    String welcomeFile = DomUtil.getText(welcomeFileNode);
                    welcomeFiles.add(welcomeFile);

                    log.debug("Adding welcome-file: " + welcomeFile);
                }
            }

            welcomeFilesPopulated = true;
        }
        catch (Exception ex)
        {
            log.warn("Failed to calculate welcome files from web.xml.", ex);
        }
    }

    /**
     * Use the default list of components to strip to normalize a filename
     */
    protected void initDefaultWelcomeFileList()
    {
        log.debug("Using default welcome file list (index.[jsp|htm[l]])");

        welcomeFiles.add("index.html");
        welcomeFiles.add("index.htm");
        welcomeFiles.add("index.jsp");

        welcomeFilesPopulated = true;
    }

    /**
     * Accessor for the list of components to strip to normalize a filename
     * @param welcomeFiles the welcomeFiles to set
     */
    public void setWelcomeFileList(List welcomeFiles)
    {
        this.welcomeFiles = welcomeFiles;
        welcomeFilesPopulated = true;
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
        welcomeFilesPopulated = true;
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
     * Does the page normalizer include query strings in it's definition of
     * pages?
     */
    protected boolean normalizeIncludesQueryString = false;

    /**
     * How we create new documents
     */
    protected DocumentBuilderFactory buildFactory = null;

    /**
     * The list of filename components to strip to normalize a filename
     */
    protected List welcomeFiles = new ArrayList();

    /**
     * Have we got some welcomFile list from somewhere?
     */
    protected boolean welcomeFilesPopulated = false;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultPageNormalizer.class);
}
