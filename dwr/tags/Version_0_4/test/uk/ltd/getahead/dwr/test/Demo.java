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
            return new int[]
            {
                1000, 2000, 3000, 4000
            };
        }
        else
        {
            return new int[]
            {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
            };
        }
    }

    /**
     * Get information about the host server.
     * @return string host information
     */
    public String getServerInfo()
    {
        return ExecutionContext.get().getServletContext().getServerInfo() + " running on JDK " + System.getProperty("java.specification.version"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Get information about the current version of DWR.
     * @return string host information
     */
    public String getDWRInfo()
    {
        return ExecutionContext.get().getVersion();
    }

    /**
     * Get information about the use of this page.
     * @return string click information.
     */
    public String getClicks()
    {
        HttpSession session = ExecutionContext.get().getSession();
        ServletContext context = ExecutionContext.get().getServletContext();

        Integer contextClicks = (Integer) context.getAttribute(CONTEXT_CLICKS);
        Integer sessionClicks = (Integer) session.getAttribute(SESSION_CLICKS);

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

        context.setAttribute(CONTEXT_CLICKS, contextClicks);
        session.setAttribute(SESSION_CLICKS, sessionClicks);

        return "getClicks() has been clicked " + sessionClicks + " times this session and " + contextClicks + " times since the last deployment"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * Get some text that has been fetched from a JSP page.
     * @return A part of a web page
     * @throws IOException 
     * @throws ServletException 
     */
    public String getForward() throws ServletException, IOException
    {
        return ExecutionContext.get().forwardToString("/forward.jsp"); //$NON-NLS-1$
    }

    private static final String LINE4 = "line4"; //$NON-NLS-1$
    private static final String LINE3 = "line3"; //$NON-NLS-1$
    private static final String LINE2 = "line2"; //$NON-NLS-1$
    /**
     * @param postcode the code to lookup
     * @return a map of postcode data
     */
    public Map fillAddress(String postcode)
    {
        Map reply = new HashMap();

        if (postcode.equalsIgnoreCase("LE16 7TR")) //$NON-NLS-1$
        {
            reply.put(LINE2, "Church Lane"); //$NON-NLS-1$
            reply.put(LINE3, "Thorpe Langton"); //$NON-NLS-1$
            reply.put(LINE4, "MARKET HARBOROUGH"); //$NON-NLS-1$
        }

        else if (postcode.equalsIgnoreCase("NR14 7SL")) //$NON-NLS-1$
        {
            reply.put(LINE2, "Rectory Lane"); //$NON-NLS-1$
            reply.put(LINE3, "Poringland"); //$NON-NLS-1$
            reply.put(LINE4, "NORWICH"); //$NON-NLS-1$
        }

        else if (postcode.equalsIgnoreCase("B92 7TT")) //$NON-NLS-1$
        {
            reply.put(LINE2, "Olton Mere"); //$NON-NLS-1$
            reply.put(LINE3, "Warwick Road"); //$NON-NLS-1$
            reply.put(LINE4, "SOLIHULL"); //$NON-NLS-1$
        }
        
        else if (postcode.equalsIgnoreCase("E17 8YT")) //$NON-NLS-1$
        {
            reply.put(LINE2, ""); //$NON-NLS-1$
            reply.put(LINE3, "PO Box 43108 "); //$NON-NLS-1$
            reply.put(LINE4, "LONDON"); //$NON-NLS-1$
        }
        
        else if (postcode.equalsIgnoreCase("SN4 8QS")) //$NON-NLS-1$
        {
            reply.put(LINE2, "Binknoll"); //$NON-NLS-1$
            reply.put(LINE3, "Wootton Bassett"); //$NON-NLS-1$
            reply.put(LINE4, "SWINDON"); //$NON-NLS-1$
        }
        
        else if (postcode.equalsIgnoreCase("NN5 7HT")) //$NON-NLS-1$
        {
            reply.put(LINE2, "Heathville"); //$NON-NLS-1$
            reply.put(LINE3, ""); //$NON-NLS-1$
            reply.put(LINE4, "NORTHAMPTON"); //$NON-NLS-1$
        }

        else
        {
            reply.put(LINE2, "Postcode not found"); //$NON-NLS-1$
            reply.put(LINE3, ""); //$NON-NLS-1$
            reply.put(LINE4, ""); //$NON-NLS-1$
        }

        return reply;
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
        Person fred = new Person("Fred", new Date().toString(), 2, 100000.0f); //$NON-NLS-1$
        Person jim = new Person("Jim", new Date().toString(), 3, 20000.0f); //$NON-NLS-1$
        Person shiela = new Person("Shiela", new Date().toString(), 4, 3000000.0f); //$NON-NLS-1$

        people.put(new Integer(fred.getId()), fred);
        people.put(new Integer(jim.getId()), jim);
        people.put(new Integer(shiela.getId()), shiela);
    }

    private static final String SESSION_CLICKS = "sessionClicks"; //$NON-NLS-1$

    private static final String CONTEXT_CLICKS = "contextClicks"; //$NON-NLS-1$

    private static int nextId = 10;

    private final Map people = new HashMap();

    private Person person = new Person("John Doe", new Date().toString(), 1, 100000.0F); //$NON-NLS-1$
}
