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
package uk.ltd.getahead.dwrdemo.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Battleships
{
    /**
     * @return The new you
     */
    public Person initPerson()
    {
        WebContext webContext = WebContextFactory.get();
        ScriptSession scriptSession = webContext.getScriptSession();

        Person person = (Person) scriptSession.getAttribute("person");
        if (person != null)
        {
            throw new IllegalArgumentException("Person exists: name=" + person);
        }

        person = new Person();
        scriptSession.setAttribute("person", person);

        return person;
    }

    /**
     * @return The current grid size
     */
    public Position getGridSize()
    {
        return Position.FULL_GRID;
    }

    /**
     * @param name The new name
     */
    public void setName(String name)
    {
        Person person = getPerson();
        addMessageToList(person + " has become " + name, person, true);
        person.setName(name);
        refreshEveryone();
    }

    /**
     * @param position Where we are moving to
     * @return The current person
     */
    public Person move(Position position)
    {
        Person person = getPerson();
        person.setPosition(position);
        return person;
    }

    /**
     * @param position Where we are shooting
     */
    public void shoot(Position position)
    {
        Person me = getPerson();
        addMessageToList(me + " shoots at " + position, me, true);

        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        Collection othersSessions = wctx.getScriptSessionsByPage(currentPage);

        for (Iterator it = othersSessions.iterator(); it.hasNext();)
        {
            ScriptSession theirSession = (ScriptSession) it.next();
            Person him = (Person) theirSession.getAttribute("person");
            if (him != null && position.equals(him.getPosition()))
            {
                if (me == him)
                {
                    me.setScore(me.getScore() - 1);

                    String text = me + " fragged himself!";
                    addMessageToList(text, system, true);
                }
                else
                {
                    me.setScore(me.getScore() + 1);
                    him.setScore(him.getScore() - 1);

                    String text = me + " fragged " + him;
                    addMessageToList(text, system, true);
                }
            }
        }

        refreshEveryone();
    }

    /**
     * @param text
     */
    public void addMessage(String text)
    {
        Person person = getPerson();
        addMessageToList(text, person, false);
        refreshEveryone();
    }

    /**
     * 
     */
    private void refreshEveryone()
    {
        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        // For all the browsers on the current page:
        Collection sessions = wctx.getScriptSessionsByPage(currentPage);

        // Get a list of all the players:
        List players = new ArrayList();
        for (Iterator it = sessions.iterator(); it.hasNext();)
        {
            ScriptSession session = (ScriptSession) it.next();
            Person person = (Person) session.getAttribute("person");
            if (person != null)
            {
                players.add(person);
            }
        }

        // Send the browsers the data
        ScriptProxy all = new ScriptProxy(sessions);
        ScriptBuffer buffer = new ScriptBuffer();
        buffer.appendScript("showMessagesAndScores(")
              .appendData(messages)
              .appendScript(",")
              .appendData(players)
              .appendScript(");");
        all.addScript(buffer);
    }

    /**
     * @param text
     * @param person
     * @param trusted 
     */
    private void addMessageToList(String text, Person person, boolean trusted)
    {
        // Make sure we have a list of the list 10 messages
        if (text != null && text.trim().length() > 0)
        {
            messages.addFirst(new Message(text, person, trusted));
            while (messages.size() > 20)
            {
                messages.removeLast();
            }
        }
    }

    /**
     * @return The person attached to the current script session
     */
    private Person getPerson()
    {
        WebContext webContext = WebContextFactory.get();
        ScriptSession scriptSession = webContext.getScriptSession();
        Person person = (Person) scriptSession.getAttribute("person");

        if (person == null)
        {
            throw new IllegalArgumentException("Missing person");
        }
        return person;
    }

    /**
     * 
     */
    private Person system = new Person();
    {
        system.setName("System");
        system.setColor("#eee");
    }

    /**
     * The current set of messages
     */
    protected LinkedList messages = new LinkedList();
}
