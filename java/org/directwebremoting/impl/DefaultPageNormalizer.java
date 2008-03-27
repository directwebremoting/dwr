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
        if (welcomeFiles == null)
        {
            if (!initWebXmlWelcomeFileList())
            {
                initDefaultWelcomeFileList();
            }
        }

        if (unnormalized == null)
        {
            return null;
        }

        String normalized = unnormalized;
        for (Iterator it = welcomeFiles.iterator(); it.hasNext();)
        {
            String welcomeFile = (String) it.next();
            if (unnormalized.endsWith(welcomeFile))
            {
                normalized = unnormalized.substring(0, unnormalized.length() - welcomeFile.length());
                break;
            }
        }

        return normalized;
    }

    /**
     * Accessor for the list of components to strip to normalize a filename
     * @return True if we successfully configured a welcome file list
     */
    public boolean initWebXmlWelcomeFileList()
    {
        try
        {
            ServletContext context = WebContextFactory.get().getServletContext();
            InputStream in = context.getResourceAsStream(PathConstants.RESOURCE_WEB_XML);
            if (in == null)
            {
                log.warn("Missing " + PathConstants.RESOURCE_WEB_XML);
                return false;
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
                return false;
            }

            welcomeFiles = new ArrayList();
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

            return true;
        }
        catch (Exception ex)
        {
            log.warn("Failed to calculate welcome files from web.xml.", ex);
            return false;
        }
    }

    /**
     * Use the default list of components to strip to normalize a filename
     */
    public void initDefaultWelcomeFileList()
    {
        log.debug("Using default welcome file list (index.[jsp|htm[l]])");

        welcomeFiles = new ArrayList();
        welcomeFiles.add("index.html");
        welcomeFiles.add("index.htm");
        welcomeFiles.add("index.jsp");
    }

    /**
     * Accessor for the list of components to strip to normalize a filename
     * @param welcomeFiles the welcomeFiles to set
     */
    public void setWelcomeFileList(List welcomeFiles)
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
        welcomeFiles = new ArrayList();
        StringTokenizer st = new StringTokenizer(welcomeFileNames, "\n,");
        while (st.hasMoreTokens())
        {
            welcomeFiles.add(st.nextToken());
        }
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultPageNormalizer.class);

    /**
     * How we create new documents
     */
    private DocumentBuilderFactory buildFactory = null;

    /**
     * The list of filename components to strip to normalize a filename
     */
    private List welcomeFiles = null;
}
