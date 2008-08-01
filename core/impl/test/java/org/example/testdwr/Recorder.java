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
package org.example.testdwr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Recorder
{
    private static final String RECORD_FILE = ".dwr_test_results.xml";

    public void postResults(int testCount, int passCount, int failCount, Collection<TestResult> data) throws ValidityException, ParsingException, IOException
    {
        String userHome = System.getProperty("user.home");
        File userDir = new File(userHome);
        File recordFile = new File(userDir, RECORD_FILE);

        Document doc = null;
        Element results = null;

        if (recordFile.exists())
        {
            Builder builder = new Builder();
            doc = builder.build(recordFile);
            results = doc.getRootElement();
        }
        else
        {
            results = new Element("results");
            doc = new Document(results);
        }

        WebContext webContext = WebContextFactory.get();
        ServletContext servletContext = webContext.getServletContext();
        HttpServletRequest request = webContext.getHttpServletRequest();

        Element result = new Element("testRun");
        result.addAttribute(new Attribute("serverInfo", servletContext.getServerInfo()));
        result.addAttribute(new Attribute("dwrVersion", webContext.getVersion()));
        result.addAttribute(new Attribute("userAgent", request.getHeader("User-Agent")));
        result.addAttribute(new Attribute("testCount", Integer.toString(testCount)));
        result.addAttribute(new Attribute("passCount", Integer.toString(passCount)));
        result.addAttribute(new Attribute("failCount", Integer.toString(failCount)));
        result.addAttribute(new Attribute("javaVersion", System.getProperty("java.version")));

        for (TestResult datum : data)
        {
            Element failure = new Element("failure");
            failure.addAttribute(new Attribute("testName", datum.getName()));
            failure.addAttribute(new Attribute("status", datum.getStatus()));
            StringBuilder buffer = new StringBuilder();
            for (String message : datum.getMessages())
            {
                buffer.append(message);
                buffer.append("\n");
            }
            failure.appendChild(buffer.toString());
        }
        results.appendChild(result);

        Serializer serializer = new Serializer(new FileOutputStream(recordFile));
        serializer.setIndent(2);
        serializer.write(doc);
    }
}
