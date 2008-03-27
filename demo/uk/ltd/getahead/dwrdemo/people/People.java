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
package uk.ltd.getahead.dwrdemo.people;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.directwebremoting.util.Logger;

/**
 * A container for a set of people
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class People
{
    /**
     * Pre-populate with random people
     */
    public People()
    {
        log.debug("Generating a new set of random people"); //$NON-NLS-1$
        for (int i = 0; i < 5; i++)
        {
            people.add(getRandomPerson());
        }
    }

    /**
     * Accessor for the current list of people
     * @return the current list of people
     */
    public Set getAllPeople()
    {
        return people;
    }

    /**
     * Insert a person into the set of people
     * @param person The person to add or update
     */
    public void setPerson(Person person)
    {
        log.debug("Adding person: " + person); //$NON-NLS-1$
        if (person.getId() == -1)
        {
            person.setId(getNextId());
        }

        people.remove(person);
        people.add(person);
    }

    /**
     * Delete a person from the set of people
     * @param person The person to delete
     */
    public void deletePerson(Person person)
    {
        log.debug("Removing person: " + person); //$NON-NLS-1$
        people.remove(person);
        debug();
    }

    /**
     * the current list of people
     */
    private Set people = new HashSet();

    /**
     * Create a random person
     * @return a random person
     */
    private Person getRandomPerson()
    {
        Person person = new Person();
        person.setId(getNextId());

        String firstname = FIRSTNAMES[random.nextInt(FIRSTNAMES.length)];
        String surname = SURNAMES[random.nextInt(SURNAMES.length)];
        person.setName(firstname + " " + surname); //$NON-NLS-1$

        String housenum = (random.nextInt(99) + 1) + " "; //$NON-NLS-1$
        String road1 = ROADS1[random.nextInt(ROADS1.length)];
        String road2 = ROADS2[random.nextInt(ROADS2.length)];
        String town = TOWNS[random.nextInt(TOWNS.length)];
        String address = housenum + road1 + " " + road2 + ", " + town; //$NON-NLS-1$ //$NON-NLS-2$
        person.setAddress(address);

        float salary = Math.round(10 + 90 * random.nextFloat()) * 1000;
        person.setSalary(salary);

        return person;
    }

    /**
     * List the current people so we know what is going on
     */
    protected void debug()
    {
        for (Iterator it = people.iterator(); it.hasNext();)
        {
            Person person = (Person) it.next();
            log.debug(person.toString());
        }
    }

    /**
     * Get the next unique ID in a thread safe way
     * @return a unique id
     */
    private static synchronized int getNextId()
    {
        return nextId++;
    }

    /**
     * The next ID, to get around serialization issues
     */
    private static int nextId = 1;

    private Random random = new Random();

    private static final String[] FIRSTNAMES = 
    {
        "Fred", "Jim", "Shiela", "Jack", "Betty", "Jacob", "Martha", "Kelly", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        "Luke", "Matt", "Gemma", "Joe", "Ben", "Jessie", "Leanne", "Becky", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
    };

    private static final String[] SURNAMES = 
    {
        "Sutcliffe", "MacDonald", "Duckworth", "Smith", "Wisner", "Iversen", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        "Nield", "Turton", "Trelfer", "Wilson", "Johnson", "Cowan", "Daniels", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    };

    private static final String[] ROADS1 =
    {
        "Green", "Red", "Yellow", "Brown", "Blue", "Black", "White", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    };

    private static final String[] ROADS2 =
    {
        "Close", "Drive", "Street", "Avenue", "Crescent", "Road", "Place", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    };

    private static final String[] TOWNS =
    {
        "Birmingham", "Kettering", "Paris", "San Francisco", "New York", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        "San Mateo", "Barcelona", //$NON-NLS-1$ //$NON-NLS-2$
    };

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(People.class);
}
