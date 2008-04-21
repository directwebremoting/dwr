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
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.example.dwr.util.RandomData;

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
        init(5);
    }

    /**
     * 
     */
    public void init(int count)
    {
        for (int i = 0; i < count; i++)
        {
            people.add(getRandomPerson());
        }
    }

    /**
     * Accessor for the current list of people
     * @return the current list of people
     */
    public List<Person> getAllPeople()
    {
        return people;
    }

    /**
     * Accessor for a subset of the current list of people
     * @return the current list of people
     */
    public List<Person> getMatchingPeople(String filter)
    {
        List<Person> reply = new ArrayList<Person>();
        Pattern regex = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
        for (Person person : people)
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
    public void setPerson(Person person)
    {
        log.debug("Adding person: " + person);
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
        log.debug("Removing person: " + person);
        people.remove(person);
        debug();
    }

    /**
     * Create a random person
     * @return a random person
     */
    public static Person getRandomPerson()
    {
        Person person = new Person();
        person.setId(getNextId());
        person.setName(RandomData.getFullName());
        String[] addressAndNumber = RandomData.getAddressAndNumber();
        person.setAddress(addressAndNumber[0]);
        person.setPhoneNumber(addressAndNumber[1]);
        person.setSalary(RandomData.getSalary());
        return person;
    }

    /**
     * List the current people so we know what is going on
     */
    protected void debug()
    {
        for (Person person : people)
        {
            log.debug(person.toString());
        }
    }

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
     * the current list of people
     */
    private List<Person> people =  Collections.synchronizedList(new ArrayList<Person>());

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(People.class);
}
