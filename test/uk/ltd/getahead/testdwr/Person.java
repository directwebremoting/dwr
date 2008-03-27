package uk.ltd.getahead.testdwr;

import java.util.Date;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Person
{
    /**
     * Default Constructor
     */
    public Person()
    {
    }

    /**
     * @param name
     * @param dateOfBirth
     * @param id
     * @param salary
     */
    public Person(String name, String dateOfBirth, int id, float salary)
    {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.id = id;
        this.salary = salary;
    }

    /**
     * @return Returns the dateOfBirth.
     */
    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth The dateOfBirth to set.
     */
    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the salary.
     */
    public float getSalary()
    {
        return salary;
    }

    /**
     * @param salary The salary to set.
     */
    public void setSalary(float salary)
    {
        this.salary = salary;
    }

    private String name = ""; //$NON-NLS-1$
    private String dateOfBirth = new Date().toString();
    private float salary = 0.0f;
    private int id = -1;
}
