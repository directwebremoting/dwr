/* * Copyright 2005 Joe Walker * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at * *     http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */package org.directwebremoting.server.tomcat;import org.apache.catalina.Context;import org.apache.catalina.Engine;import org.apache.catalina.Host;import org.apache.catalina.connector.Connector;import org.apache.catalina.startup.Embedded;/** * Launch Tomcat embedded. * @author Joe Walker [joe at getahead dot org] */public class TomcatDemoLauncher{    /**     * Path (can be relative) to the web application (aka context)     * This directory should contain a WEB-INF/web.xml file     */    public static final String CONTEXT_HOME = "target/ant/web/demo";    /**     * URL component to which we deploy the application, which goes something     * like this: http://example.com/CONTEXT_PATH/path_to_something_in_the_webapp     */    public static final String CONTEXT_PATH = "/dwr";    /**     * The port to listen on     */    public static final int PORT = 8080;    /**     * Just create and launch an instance of Jetty     * @param args program args. Ignored.     * @throws Exception This is such a small program we ignore exceptions     */    public static void main(String[] args) throws Exception    {        TomcatDemoLauncher launcher = new TomcatDemoLauncher(CONTEXT_HOME, CONTEXT_PATH, PORT);        launcher.start();    }    /**     * Sets up the server.     * @param contextHome See comments for {@link #CONTEXT_HOME}     * @param contextPath See comments for {@link #CONTEXT_PATH}     * @throws Exception This is such a small program we ignore exceptions     */    public TomcatDemoLauncher(String contextHome, final String contextPath, int port) throws Exception    {        embedded = new Embedded();        embedded.setCatalinaBase(".");        Engine engine = embedded.createEngine();        Host host = embedded.createHost("localhost", ".");        engine.addChild(host);        Context context = embedded.createContext(contextPath, contextHome);        host.addChild(context);        embedded.addEngine(engine);        Connector connector = embedded.createConnector("localhost", port, false);        embedded.addConnector(connector);    }    /**     * Start the web serving/file scanning threads     * @throws Exception This is such a small program we ignore exceptions     */    private void start() throws Exception    {        embedded.start();    }    /**     * The embedded component coordinator     */    private final Embedded embedded;}