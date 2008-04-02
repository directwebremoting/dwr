package org.directwebremoting.convert.mapped;

import org.directwebremoting.util.CompareUtil;

/**
 * A hibernate bean to fit the declaration in {@link org.directwebremoting.hibernate.Database}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Hibernate3NestEx
{
    public Hibernate3NestEx()
    {
    }

    public Hibernate3NestEx(Integer id)
    {
        this.id = id;
    }

    public Hibernate3NestEx(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Hibernate3NestEx(Integer id, String name, Hibernate3Ex owner)
    {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public Hibernate3Ex getOwner()
    {
        return owner;
    }

    public void setOwner(Hibernate3Ex owner)
    {
        this.owner = owner;
    }

    @Override
    public String toString()
    {
        if (owner == null)
        {
            return "Hibernate3NestEx[id=" + getId() + ",name=" + getName() + ",owner=null]";
        }
        else
        {
            return "Hibernate3NestEx[id=" + getId() + ",name=" + getName() + ",owner=" + getOwner().getName() + "]";
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        Class<? extends Hibernate3NestEx> thisClass = this.getClass();
        Class<? extends Object> thatClass = obj.getClass();
        if (!thisClass.isAssignableFrom(thatClass) && !thatClass.isAssignableFrom(thisClass))
        {
            return false;
        }

        Hibernate3NestEx that = (Hibernate3NestEx) obj;

        if (!CompareUtil.equals(this.getId(), that.getId()))
        {
            return false;
        }

        // Normally .equals should only have to test PK for equality with a DB
        // but we want our tests to be tighter ...

        if (!CompareUtil.equals(this.getName(), that.getName()))
        {
            return false;
        }

        // We don't want to recurse
        // if (!CompareUtil.equals(this.getOwner(), that.getOwner()))
        // {
        //     return false;
        // }

        return true;
    }

    private Integer id;

    private String name;

    private Hibernate3Ex owner;
}
