package uk.ltd.getahead.testdwr;

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
     * @param address
     * @param id
     * @param salary
     */
    public Person(String name, String address, int id, float salary)
    {
        this.name = name;
        this.address = address;
        this.id = id;
        this.salary = salary;
    }

    /**
     * @return Returns the address.
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address The address to set.
     */
    public void setAddress(String address)
    {
        this.address = address;
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
    private String address = ""; //$NON-NLS-1$
    private float salary = 0.0f;
    private int id = -1;
}
