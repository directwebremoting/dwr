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
package com.example.dwr.people;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.datasync.Directory;
import org.directwebremoting.datasync.MapStoreProvider;


/**
 * A container for a set of people
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class People
{
    /**
     * Pre-populate with 50 random people
     */
    public People()
    {
        this(1000);
    }

    /**
     * Pre-populate with <code>count</code> random people
     */
    public People(int count)
    {
        people = createCrowd(count);
        MapStoreProvider<Person> provider = new MapStoreProvider<Person>(people, Person.class);

        log.debug("Registering StoreProvider 'testServerData'");
        Directory.register("testServerData", provider);
    }

    /**
     * 
     */
    public Map<String, Person> createCrowd(int count)
    {
        Map<String, Person> reply = new HashMap<String, Person>();
        for (int i = 0; i < count; i++)
        {
            reply.put(getNextId(), getRandomPerson());
        }
        return reply;
    }

    /**
     * Accessor for the current list of people
     * @return the current list of people
     */
    public Collection<Person> getAllPeople()
    {
        return people.values();
    }

    /**
     * Accessor for a subset of the current list of people
     * @return the current list of people
     */
    public List<Person> getMatchingPeople(String filter)
    {
        List<Person> reply = new ArrayList<Person>();
        Pattern regex = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
        for (Person person : people.values())
        {
            if (regex.matcher(person.getName()).find())
            {
                reply.add(person);
                log.debug("Adding " + person + " to reply");
            }
        }
        return reply;
    }

    /**
     * Insert a person into the set of people
     * @param person The person to add or update
     */
    public void setPerson(String id, Person person)
    {
        log.debug("Adding person: " + person);
        if (id == null)
        {
            id = getNextId();
        }

        people.put(id, person);
    }

    /**
     * Delete a person from the set of people
     * @param id The id of the person to delete
     */
    public void deletePerson(String id)
    {
        log.debug("Removing person with id: " + id);
        people.remove(id);
        debug();
    }

    /**
     * Create a random person
     * @return a random person
     */
    public static Person getRandomPerson()
    {
        Person person = new Person();
        person.setName(RandomData.getFullName());
        String[] addressAndNumber = RandomData.getAddressAndNumber();
        person.setAddress(addressAndNumber[0]);
        person.setPhoneNumber(addressAndNumber[1]);
        person.setAge(RandomData.getAge());
        return person;
    }

    /**
     * List the current people so we know what is going on
     */
    protected void debug()
    {
        for (Person person : people.values())
        {
            log.debug(person.toString());
        }
    }

    /**
     * Get the next unique ID in a thread safe way
     * @return a unique id
     */
    public static synchronized String getNextId()
    {
        return "P" + (nextId++);
    }

    /**
     * The next ID, to get around serialization issues
     */
    private static int nextId = 1;

    /**
     * the current list of people
     */
    private final Map<String, Person> people;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(People.class);
}
