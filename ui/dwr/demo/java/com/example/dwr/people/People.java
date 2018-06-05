package com.example.dwr.people;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.directwebremoting.datasync.Directory;
import org.directwebremoting.datasync.MapStoreProvider;
import org.directwebremoting.datasync.StoreProvider;

/**
 * A container for 2 sets of people.
 * The smaller is designed for viewing all at the same time, and is viewable
 * and editable via {@link #getSmallCrowd()}, {@link #setPerson(Person)} and
 * {@link #deletePerson(String)}. The larger is accessible using the
 * {@link StoreProvider} registered under 'largeCrowd', and searchable using
 * {@link #getMatchingFromLargeCrowd(String)}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class People
{
    /**
     * Pre-populate the small and large crowds
     */
    public People()
    {
        smallCrowd = createCrowd(10);
        MapStoreProvider<Person> provider = new MapStoreProvider<Person>(createCrowd(1000), Person.class);
        Directory.register("largeCrowd", provider);
        largeCrowd = provider.asMap();
    }

    /**
     * We maintain 2 lists of people, small (~10 people) and large (~1000).
     * The smaller is for when we want to show them all on the screen at the
     * same time, the larger for when we don'e.
     */
    public Collection<Person> getSmallCrowd()
    {
        return smallCrowd.values();
    }

    /**
     * Insert a person into the set of people
     * @param person The person to add or update
     */
    public String setPerson(Person person)
    {
        smallCrowd.put(person.getId(), person);
        return "Updated values for " + person.getName();
    }

    /**
     * Delete a person from the set of people
     * @param id The id of the person to delete
     */
    public String deletePerson(String id)
    {
        Person person = smallCrowd.remove(id);
        if (person == null)
        {
            return "Person does not exist";
        }
        else
        {
            return "Deleted " + person.getName();
        }
    }

    /**
     * Accessor for a subset of the current list of people
     * @return the current list of people
     */
    public List<Person> getMatchingFromLargeCrowd(String filter)
    {
        List<Person> reply = new ArrayList<Person>();
        Pattern regex = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
        for (Person person : largeCrowd.values())
        {
            if (regex.matcher(person.getName()).find())
            {
                reply.add(person);
            }
        }
        return reply;
    }

    /**
     * @see #getSmallCrowd()
     */
    private final Map<String, Person> smallCrowd;

    /**
     * @see #getMatchingFromLargeCrowd(String)
     */
    private final Map<String, Person> largeCrowd;

    /**
     * Both crowds are created in the same way.
     */
    private static Map<String, Person> createCrowd(int count)
    {
        Map<String, Person> reply = new HashMap<String, Person>();
        for (int i = 0; i < count; i++)
        {
            Person person = new Person(true);
            reply.put(person.getId(), person);
        }
        return reply;
    }
}
