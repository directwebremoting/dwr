package uk.ltd.getahead.dwr.test;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * Examples for the demo pages.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Demo
{
    /**
     * Default ctor
     */
    public Demo()
    {
        createPeople();
    }

    /**
     * If you change this you should change list.html at the same time.
     * @param big do you want the numbers to be big?
     * @return an array of numbers
     */
    public int[] getNumbers(boolean big)
    {
        if (big)
        {
            return new int[] { 1000, 2000, 3000, 4000 };
        }
        else
        {
            return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        }
    }

    /**
     * Get information about the host server.
     * @return string host information
     */
    public String getServerInfo()
    {
        return ExecutionContext.getExecutionContext().getServletContext().getServerInfo();
    }

    /**
     * Get information about the use of this page.
     * @return string click information.
     */
    public String getClicks()
    {
        HttpSession session = ExecutionContext.getExecutionContext().getSession();
        ServletContext context = ExecutionContext.getExecutionContext().getServletContext();

        Integer contextClicks = (Integer) context.getAttribute("contextClicks");
        Integer sessionClicks = (Integer) session.getAttribute("sessionClicks");

        if (contextClicks == null)
        {
            contextClicks = new Integer(0);
        }

        if (sessionClicks == null)
        {
            sessionClicks = new Integer(0);
        }

        contextClicks = new Integer(contextClicks.intValue() + 1);
        sessionClicks = new Integer(sessionClicks.intValue() + 1);

        context.setAttribute("contextClicks", contextClicks);
        session.setAttribute("sessionClicks", sessionClicks);

        return "getClicks() has been clicked "+sessionClicks+" times this session and "+contextClicks+" times since the last deployment";
    }

    /**
     * Get some text that has been fetched from a JSP page.
     * @return A part of a web page
     * @throws IOException 
     * @throws ServletException 
     */
    public String getForward() throws ServletException, IOException
    {
        return ExecutionContext.getExecutionContext().forwardToString("/forward.jsp");
    }

    /**
     * Get a single example person POJO
     * @return a Person
     */
    public Person getExamplePerson()
    {
        return person;
    }

    /**
     * Set the single example person POJO
     * @param person the example person
     */
    public void setExamplePerson(Person person)
    {
        this.person = person;
    }

    /**
     * Get an array of known people.
     * @return a collection of people.
     */
    public Collection getAllPeople()
    {
        return people.values();
    }

    /**
     * Get a single person POJO
     * @param id The id of the person to retrieve
     * @return a Person
     */
    public Person getPerson(int id)
    {
        return (Person) people.get(new Integer(id));
    }

    /**
     * Remove a person from our list of all people
     * @param toDelete The person to delete (id being the match field)
     * @return true if the person was deleted.
     */
    public boolean deletePerson(Person toDelete)
    {
        return people.remove(new Integer(toDelete.getId())) != null;
    }

    /**
     * Add a person to our list of all people
     * @param toAdd The person to add
     */
    public void addPerson(Person toAdd)
    {
        if (toAdd.getId() == -1)
        {
            toAdd.setId(nextId++);
        }

        people.put(new Integer(toAdd.getId()), toAdd);
    }

    /**
     * Setup the list of people.
     */
    private void createPeople()
    {
        Person fred = new Person("Fred", new Date().toString(), 2, 100000.0f);
        Person jim = new Person("Jim", new Date().toString(), 3, 20000.0f);
        Person shiela = new Person("Shiela", new Date().toString(), 4, 3000000.0f);

        people.put(new Integer(fred.getId()), fred);
        people.put(new Integer(jim.getId()), jim);
        people.put(new Integer(shiela.getId()), shiela);
    }

    private static int nextId = 10;
    private final Map people = new HashMap();
    private Person person = new Person("John Doe", new Date().toString(), 1, 100000.0F);
}
