package org.getahead.dwrdemo.gidemo.bus;

public class Country
{
    public Country()
    {
    }

    public Country(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private String id;
    private String name;
}
