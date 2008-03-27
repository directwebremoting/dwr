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
package org.getahead.dwrdemo.fluent;

import org.directwebremoting.fluent.FluentConfigurator;

/**
 * An equivalent of dwr.xml for demo purposes
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DemoFluentConfigurator extends FluentConfigurator
{
    /* (non-Javadoc)
     * @see org.directwebremoting.fluent.FluentConfigurator#configure()
     */
    @Override
    public void configure()
    {
        /*
        withFilter("org.getahead.dwrdemo.monitor.MonitoringAjaxFilter");
        withFilter("org.directwebremoting.filter.ExtraLatencyAjaxFilter")
            .addParam("delay", "200");
        */

        // intro - for the test on index.html
        withCreator("new", "Intro")
            .addParam("class", "org.getahead.dwrdemo.intro.Intro");

        // address
        withCreator("new", "AddressLookup")
            .addParam("class", "org.getahead.dwrdemo.address.AddressLookup");

        // chat
        withCreator("new", "JavascriptChat")
            .addParam("scope", "application")
            .addParam("class", "org.getahead.dwrdemo.chat.JavascriptChat");
        withCreator("new", "JavaChat")
            .addParam("scope", "application")
            .addParam("class", "org.getahead.dwrdemo.chat.JavaChat");
        withConverter("bean", "org.getahead.dwrdemo.chat.Message");

        // clock
        withCreator("new", "Clock")
            .addParam("scope", "application")
            .addParam("class", "org.getahead.dwrdemo.clock.Clock");

        // people
        withCreator("new", "People")
            .addParam("scope", "script")
            .addParam("class", "org.getahead.dwrdemo.people.People");
        withConverter("bean", "org.getahead.dwrdemo.people.Person")
            .addParam("scriptName", "Person"); /* TODO: make this work */

        // simple text
        withCreator("new", "Demo")
            .addParam("class", "org.getahead.dwrdemo.simpletext.Demo");

        // resources not in this war file: java.util.Date
        withCreator("new", "JDate")
            .addParam("scope", "session")
            .addParam("class", "java.util.Date")
            .exclude("getHours")
            .withAuth("getMinutes", "admin")
            .withAuth("getMinutes", "devel")
            .addFilter("org.getahead.dwrdemo.filter.LoggingAjaxFilter");

        // TIBCO GI demos
        withCreator("new", "Publisher")
            .addParam("scope", "application")
            .addParam("class", "org.getahead.dwrdemo.gidemo.Publisher");
        withCreator("new", "Corporations")
            .addParam("scope", "application")
            .addParam("class", "org.getahead.dwrdemo.gidemo.Corporations");
        withConverter("bean", "org.getahead.dwrdemo.gidemo.Corporation");
        withCreator("new", "CallCenter")
            .addParam("scope", "application")
            .addParam("class", "org.getahead.dwrdemo.ticketcenter.CallCenter");
        withConverter("bean", "org.getahead.dwrdemo.ticketcenter.Call");
        withCreator("new", "Reverse")
            .addParam("scope", "application")
            .addParam("class", "org.getahead.dwrdemo.reverse.Reverse");

        // image generator
        withCreator("new", "UploadDownload")
            .addParam("scope", "session")
            .addParam("class", "org.getahead.dwrdemo.files.UploadDownload");

        // this is a bad idea for live, but can be useful in testing
        withConverter("exception", "java.lang.Exception");
        withConverter("bean", "java.lang.StackTraceElement");
    }
}
