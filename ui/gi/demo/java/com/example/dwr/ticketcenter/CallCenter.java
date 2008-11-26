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
package com.example.dwr.ticketcenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jsx3.GI;
import jsx3.app.Server;
import jsx3.gui.Form;
import jsx3.gui.LayoutGrid;
import jsx3.gui.Matrix;
import jsx3.gui.Select;
import jsx3.gui.TextBox;
import jsx3.xml.CdfDocument;
import jsx3.xml.Record;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ui.browser.Window;

import com.example.dwr.people.RandomData;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CallCenter implements Runnable
{
    /**
     * Create a new publish thread and start it
     */
    public CallCenter()
    {
        // Start with some calls waiting
        addRandomKnownCall();
        addRandomUnknownCall();
        addRandomUnknownCall();
        addRandomUnknownCall();

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        //noinspection ThisEscapedInObjectConstruction
        executor.scheduleAtFixedRate(this, 2, 2, TimeUnit.SECONDS);
    }

    /**
     * Called once every couple of seconds to take some random action
     */
    public void run()
    {
        try
        {
            synchronized (calls)
            {
                switch (random.nextInt(5))
                {
                case 0:
                case 1:
                    addRandomUnknownCall();
                    break;

                case 2:
                    addRandomKnownCall();
                    break;

                case 3:
                    removeRandomCall();
                    break;

                default:
                    break;
                }

                updateAll();
            }
        }
        catch (Exception ex)
        {
            log.warn("Random event failure", ex);
        }
    }

    /**
     * Called when the page first loads to ensure we have an up-to-date screen
     */
    public void load()
    {
        deselect();
        update();
    }

    /**
     *
     */
    public void alertSupervisor(Call fromWeb)
    {
        // This is the ScriptSession of the agent that wishes to alert a supervisor
        ScriptSession session = WebContextFactory.get().getScriptSession();

        // We store the ID of the call we are working on in the ScriptSession
        Object handlingId = session.getAttribute("handlingId");
        if (handlingId == null)
        {
            Window.alert("No call found");
            return;
        }

        synchronized (calls)
        {
            // Check to see that the caller has not hung up since the last update
            Call call = findCaller(handlingId);
            if (call == null)
            {
                Window.alert("That caller hung up, please select another");
                return;
            }

            // The user isn't handling this call any more
            session.removeAttribute("handlingId");

            // Update the server details from those passed in
            call.setName(fromWeb.getName());
            call.setAddress(fromWeb.getAddress());
            call.setNotes(fromWeb.getNotes());
            call.setHandlerId(null);
            call.setSupervisorAlert(true);

            // Update the screen of the current user
            deselect();

            // Update everyone else's screen
            updateAll();
        }
    }

    /**
     *
     */
    public void completeHandling(Call fromWeb)
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();

        Object handlingId = session.getAttribute("handlingId");
        if (handlingId == null)
        {
            Window.alert("No call found");
            return;
        }

        synchronized (calls)
        {
            Call call = findCaller(handlingId);
            if (call == null)
            {
                Window.alert("That caller hung up, please select another");
                return;
            }

            session.removeAttribute("handlingId");

            calls.remove(call);
            log.debug("Properly we should book a ticket for " + fromWeb.getPhoneNumber());

            deselect();
            updateAll();
        }
    }

    /**
     *
     */
    public void cancelHandling()
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();

        Object handlingId = session.getAttribute("handlingId");
        if (handlingId == null)
        {
            Window.alert("That caller hung up, please select another");
            return;
        }

        synchronized (calls)
        {
            Call call = findCaller(handlingId);
            if (call == null)
            {
                log.debug("Cancel handling of call that hung up");
                return;
            }

            session.removeAttribute("handlingId");
            call.setHandlerId(null);

            deselect();
            updateAll();
        }
    }

    /**
     *
     */
    public void beginHandling(String id)
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();

        Object handlingId = session.getAttribute("handlingId");
        if (handlingId != null)
        {
            Window.alert("Please finish handling the current call before selecting another");
            return;
        }

        synchronized (calls)
        {
            Call call = findCaller(id);
            if (call == null)
            {
                log.debug("Caller not found: " + id);
                Window.alert("That caller hung up, please select another");
            }
            else
            {
                if (call.getHandlerId() != null)
                {
                    Window.alert("That call is being handled, please select another");
                    return;
                }

                session.setAttribute("handlingId", id);
                call.setHandlerId(session.getId());

                select(call);
                updateAll();
            }
        }
    }

    /**
     *
     */
    private Call findCaller(Object attribute)
    {
        try
        {
            int id = Integer.parseInt(attribute.toString());

            // We could optimize this, but since there are less than 20 people
            // in the queue ...
            for (Call call : calls)
            {
                if (call.getId() == id)
                {
                    return call;
                }
            }

            return null;
        }
        catch (NumberFormatException ex)
        {
            log.warn("Illegal number format: " + attribute.toString(), ex);
            return null;
        }
    }

    /**
     *
     */
    protected void removeRandomCall()
    {
        if (!calls.isEmpty())
        {
            synchronized (calls)
            {
                int toDelete = random.nextInt(calls.size());
                Call removed = calls.remove(toDelete);

                String sessionId = removed.getHandlerId();
                if (sessionId != null)
                {
                    Browser.withSession(sessionId, new Runnable()
                    {
                        public void run()
                        {
                            ScriptSessions.removeAttribute("handlingId");
                            Window.alert("It appears that this caller has hung up. Please select another.");
                            deselect();
                        }
                    });
                }

                updateAll();
            }

            // log.info("Random Event: Caller hangs up: " + removed.getPhoneNumber());
        }
    }

    /**
     *
     */
    protected void addRandomKnownCall()
    {
        if (calls.size() < 10)
        {
            Call call = new Call();
            call.setId(getNextId());
            call.setName(RandomData.getFullName());
            String[] addressAndNumber = RandomData.getAddressAndNumber();
            call.setAddress(addressAndNumber[0]);
            call.setPhoneNumber(addressAndNumber[1]);

            calls.add(call);

            // log.info("Random Event: New caller: " + call.getName());
        }
    }

    /**
     *
     */
    protected void addRandomUnknownCall()
    {
        if (calls.size() < 10)
        {
            String phoneNumber = RandomData.getPhoneNumber(random.nextInt(3) != 0);
            Call call = new Call();
            call.setPhoneNumber(phoneNumber);
            call.setId(getNextId());
            calls.add(call);

            // log.info("Random Event: New caller: " + call.getPhoneNumber());
        }
    }

    /**
     *
     */
    protected void updateAll()
    {
        String contextPath = ServerContextFactory.get().getContextPath();
        if (contextPath == null)
        {
            return;
        }

        String page = contextPath + "/gi/ticketcenter.html";
        Browser.withPage(page, new Runnable()
        {
            public void run()
            {
                update();
            }
        });
    }

    /**
     *
     */
    protected void update()
    {
        // Populate a CDF document with data about our calls
        CdfDocument cdfdoc = new CdfDocument("jsxroot");
        for (Call call : calls)
        {
            cdfdoc.appendRecord(new Record(call));
        }

        // Put the CDF doc into the client side cache, and repaint the table
        Server tc = GI.getServer("ticketcenter");
        tc.getCache().setDocument("callers", cdfdoc);
        tc.getJSXByName("listCallers", Matrix.class).repaint(null);
    }

    /**
     *
     */
    private void select(Call call)
    {
        Server ticketcenter = GI.getServer("ticketcenter");

        ticketcenter.getJSXByName("textPhone", TextBox.class).setValue(call.getPhoneNumber());
        ticketcenter.getJSXByName("textName", TextBox.class).setValue(call.getName());
        ticketcenter.getJSXByName("textNotes", TextBox.class).setValue(call.getNotes());
        ticketcenter.getJSXByName("selectEvent", Select.class).setValue(null);

        setFormEnabled(true);
    }

    /**
     * Set the form to show no caller
     */
    private void deselect()
    {
        Server ticketcenter = GI.getServer("ticketcenter");

        ticketcenter.getJSXByName("textPhone", TextBox.class).setValue("");
        ticketcenter.getJSXByName("textName", TextBox.class).setValue("");
        ticketcenter.getJSXByName("textNotes", TextBox.class).setValue("");
        ticketcenter.getJSXByName("selectEvent", Select.class).setValue(null);

        setFormEnabled(false);
    }

    /**
     * Disable all the elements in the form
     * @param enabled True to enable the elements/false ...
     */
    private void setFormEnabled(boolean enabled)
    {
        int state = enabled ? Form.STATEENABLED : Form.STATEDISABLED;

        Server ticketcenter = GI.getServer("ticketcenter");
        for (String element : ELEMENTS)
        {
            ticketcenter.getJSXByName(element, TextBox.class).setEnabled(state, true);
        }

        LayoutGrid layoutForm = ticketcenter.getJSXByName("layoutForm", LayoutGrid.class);
        layoutForm.setBackgroundColor(enabled ? "#FFF" : "#EEE", true);
    }

    /**
     * The form fields that we enable and disable
     */
    private static final String[] ELEMENTS = new String[]
    {
        "textPhone", "textName", "textAddress", "textPayment", "textNotes",
        "selectEvent", "selectPaymentType", "buttonBook", "buttonSupervisor",
        "buttonCancel"
    };

    /**
     * Get the next unique ID in a thread safe way
     * @return a unique id
     */
    public static synchronized int getNextId()
    {
        return nextId++;
    }

    /**
     * The next ID, to get around serialization issues
     */
    private static int nextId = 1;

    /**
     * The set of people in our database
     */
    private final List<Call> calls = Collections.synchronizedList(new ArrayList<Call>());

    /**
     * Used to generate random data
     */
    private final Random random = new Random();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(CallCenter.class);
}
