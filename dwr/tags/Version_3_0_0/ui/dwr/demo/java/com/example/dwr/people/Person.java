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

import java.util.Random;

import org.directwebremoting.datasync.ExposeToString;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@ExposeToString
public class Person
{
    /**
     * Create a blank person
     */
    public Person()
    {
        this.id = getNextId();
    }

    /**
     * Create a person with random values
     */
    public Person(boolean withRandom)
    {
        if (withRandom)
        {
            this.name = RandomData.getFullName();
            this.address = RandomData.getAddress();
            this.age = RandomData.getAge();
            this.superhero = random.nextInt(100) == 1;
        }

        this.id = getNextId();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    private String id;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    private String name;

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    private String address;

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    private int age;

    public boolean isSuperhero() { return superhero; }
    public void setSuperhero(boolean superhero) { this.superhero = superhero; }
    private boolean superhero;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return name;
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

    private static final Random random = new Random();
}
