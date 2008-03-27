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
package org.getahead.dwrdemo.ticketcenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.browser.Window;
import org.directwebremoting.util.SharedObjects;
import org.getahead.dwrdemo.util.RandomData;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CallCenter
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

        Runnable runnable = new UpdateRunnable();
        ScheduledThreadPoolExecutor executor = SharedObjects.getScheduledThreadPoolExecutor();
        executor.scheduleAtFixedRate(runnable, 2, 2, TimeUnit.SECONDS);
    }

    /**
     * Called once every couple of seconds to take some random action
     */
    protected class UpdateRunnable implements Runnable
    {
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
    
                    update();
                }
            }
            catch (Exception ex)
            {
                log.warn("Random event failure", ex);
            }
        }
    }

    /**
     * 
     */
    public void alertSupervisor(Call fromWeb)
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        Window window = new Window(session);
        int handlingId = getHandlingId(session);

        if (handlingId == -1)
        {
            window.alert("No call found");
            return;
        }

        synchronized (calls)
        {
            Call call = findCaller(handlingId);
            if (call == null)
            {
                window.alert("That caller hung up, please select another");
                return;
            }

            session.removeAttribute("handlingId");

            call.setName(fromWeb.getName());
            call.setAddress(fromWeb.getAddress());
            call.setNotes(fromWeb.getNotes());
            call.setHandlerId(null);
            call.setSupervisorAlert(true);

            deselect(session);
            update();
        }
    }

    /**
     * 
     */
    public void completeHandling(Call fromWeb)
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        Window window = new Window(session);
        int handlingId = getHandlingId(session);

        if (handlingId == -1)
        {
            window.alert("No call found");
            return;
        }

        synchronized (calls)
        {
            Call call = findCaller(handlingId);
            if (call == null)
            {
                window.alert("That caller hung up, please select another");
                return;
            }

            session.removeAttribute("handlingId");

            calls.remove(call);
            log.debug("Properly we should book a ticket for " + fromWeb.getPhoneNumber());

            deselect(session);
            update();
        }
    }

    /**
     * 
     */
    public void cancelHandling()
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        Window window = new Window(session);
        int handlingId = getHandlingId(session);

        if (handlingId == -1)
        {
            window.alert("That caller hung up, please select another");
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

            deselect(session);
            update();
        }
    }

    /**
     * 
     */
    public void beginHandling(int id)
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        Window window = new Window(session);
        int handlingId = getHandlingId(session);

        if (handlingId != -1)
        {
            window.alert("Please finish handling the current call before selecting another");
            return;
        }

        synchronized (calls)
        {
            Call call = findCaller(id);
            if (call == null)
            {
                log.debug("Caller not found: " + id);
                window.alert("That caller hung up, please select another");
            }
            else
            {
                if (call.getHandlerId() != null)
                {
                    window.alert("That call is being handled, please select another");
                    return;
                }

                session.setAttribute("handlingId", id);
                call.setHandlerId(session.getId());

                select(session, call);
                update();
            }
        }
    }

    /**
     * 
     */
    private Call findCaller(int id)
    {
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

    /**
     * 
     */
    protected void removeRandomCall()
    {
        if (calls.size() > 0)
        {
            Call removed = null;

            synchronized (calls)
            {
                int toDelete = random.nextInt(calls.size());
                removed = calls.remove(toDelete);

                String sessionId = removed.getHandlerId();
                if (sessionId != null)
                {
                    ScriptSession session = ServerContextFactory.get().getScriptSessionById(sessionId);

                    if (session != null)
                    {
                        session.removeAttribute("handlingId");

                        Window window = new Window(session);
                        window.alert("It appears that this caller has hung up. Please select another.");
                        deselect(session);                    
                    }
                }

                update();
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
    protected void update()
    {
        ServerContext serverContext = ServerContextFactory.get();
        String contextPath = serverContext.getContextPath();

        Collection<ScriptSession> sessions = serverContext.getScriptSessionsByPage(contextPath + "/gi/ticketcenter.html");

        Server ticketcenter = GI.getServer(sessions, "ticketcenter");

        // Some tweaks so GI redraws everything without needing further explanation
        // We copy the id to jsxid, and ensure that the fields for the check-boxes are
        // 0 or 1 rather than true or false. We also check that any handled caller
        // is still around and has not hung up
        CdfDocument cdfdoc = new CdfDocument("jsxroot");
        Date now = new Date();
        for (Call call : calls)
        {
            Record record = new Record("" + call.getId());

            record.setAttribute("phoneNumber", call.getPhoneNumber());

            String name = call.getName();
            if (name == null || name.equals(""))
            {
                name = "?";
            }
            record.setAttribute("name", name);

            long timePlain = now.getTime() - call.getCallStarted().getTime();
            int time = 10000 * Math.round(timePlain / 10000);
            record.setAttribute("time", "" + time);

            String handled = call.getHandlerId() != null ? "JSXAPPS/ticketcenter/images/configure.png" : "";
            record.setAttribute("handled", handled);

            String supervisorAlert = call.isSupervisorAlert() ? "JSXAPPS/ticketcenter/images/irkickflash.png" : "";
            record.setAttribute("supervisorAlert", supervisorAlert);

            cdfdoc.appendRecord(record);
        }

        // Convert the data into a CDF document and post to GI
        ticketcenter.getCache().setDocument("callers", cdfdoc);
        ticketcenter.getJSXByName("listCallers", Matrix.class).repaint(null);
    }

    /**
     * @param session
     * @param call
     */
    private void select(ScriptSession session, Call call)
    {
        Server ticketcenter = GI.getServer(session, "ticketcenter");

        ticketcenter.getJSXByName("textPhone", TextBox.class).setValue(call.getPhoneNumber());
        ticketcenter.getJSXByName("textName", TextBox.class).setValue(call.getName());
        ticketcenter.getJSXByName("textNotes", TextBox.class).setValue(call.getNotes());
        ticketcenter.getJSXByName("selectEvent", Select.class).setValue(null);

        setFormEnabled(session, true);
    }

    /**
     * Set the form to show no caller
     * @param session The user that we are clearing
     */
    private void deselect(ScriptSession session)
    {
        Server ticketcenter = GI.getServer(session, "ticketcenter");

        ticketcenter.getJSXByName("textPhone", TextBox.class).setValue("");
        ticketcenter.getJSXByName("textName", TextBox.class).setValue("");
        ticketcenter.getJSXByName("textNotes", TextBox.class).setValue("");
        ticketcenter.getJSXByName("selectEvent", Select.class).setValue(null);

        setFormEnabled(session, false);
    }

    /**
     * Disable all the elements in the form
     * @param enabled True to enable the elements/false ...
     */
    private void setFormEnabled(ScriptSession session, boolean enabled)
    {
        int state = enabled ? Form.STATEENABLED : Form.STATEDISABLED;

        Server ticketcenter = GI.getServer(session, "ticketcenter");
        for (String element : ELEMENTS)
        {
            ticketcenter.getJSXByName(element, TextBox.class).setEnabled(state, true);
        }

        LayoutGrid layoutForm = ticketcenter.getJSXByName("layoutForm", LayoutGrid.class);
        layoutForm.setBackgroundColor(enabled ? "#FFF" : "#EEE", true);
    }

    /**
     * Who is the given user looking after?
     */
    private int getHandlingId(ScriptSession session)
    {
        Object attribute = session.getAttribute("handlingId");
        if (attribute == null)
        {
            return -1;
        }

        try
        {
            return Integer.parseInt(attribute.toString());
        }
        catch (NumberFormatException ex)
        {
            log.warn("Illegal number format: " + attribute.toString(), ex);
            return -1;
        }
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
    protected List<Call> calls = Collections.synchronizedList(new ArrayList<Call>());

    /**
     * Used to generate random data
     */
    protected Random random = new Random();

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(CallCenter.class);
}
