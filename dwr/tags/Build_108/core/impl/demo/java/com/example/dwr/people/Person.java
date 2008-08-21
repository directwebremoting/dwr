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

import org.directwebremoting.io.Item;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Person implements Item.ExposeToStringToTheOutside
{
    public Person()
    {
    }

    public Person(String name, String address, int age, boolean male)
    {
        this.name = name;
        this.address = address;
        this.age = age;
        this.male = male;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    private String name;

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    private String address;

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    private String phoneNumber;

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    private int age;

    public boolean isMale() { return male; }
    public void setMale(boolean male) { this.male = male; }
    private boolean male;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return name;
    }
}
