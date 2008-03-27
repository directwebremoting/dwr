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
package org.getahead.dwrdemo.cli;

import java.io.File;
import java.net.URL;

import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

/**
 * Launch Jetty embedded.
 */
public class JettyXmlLauncher
{
    /**
     * Sets up and runs server.
     * @param args The command line arguments
     * @throws Exception Don't care because top level
     */
    public static void main(String[] args) throws Exception
    {
        System.setProperty("jetty.home", "demo/org/getahead/dwrdemo/cli");
        System.setProperty("dwr.home", ".");

        Server server = new Server();
        URL configXml = new File("demo/org/getahead/dwrdemo/cli/jetty.xml").toURL();
        XmlConfiguration configuration = new XmlConfiguration(configXml);
        configuration.configure(server);
        server.start();
    }
}
