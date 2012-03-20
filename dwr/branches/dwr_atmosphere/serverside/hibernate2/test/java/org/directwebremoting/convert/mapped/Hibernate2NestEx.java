package org.directwebremoting.convert.mapped;

import org.directwebremoting.util.CompareUtil;

/**
 * A hibernate bean to fit the declaration in {@link org.directwebremoting.hibernate.Database}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Hibernate2NestEx
{
    public Hibernate2NestEx()
    {
    }

    public Hibernate2NestEx(Integer id)
    {
        this.id = id;
    }

    public Hibernate2NestEx(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Hibernate2NestEx(Integer id, String name, Hibernate2Ex owner)
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

    public Hibernate2Ex getOwner()
    {
        return owner;
    }

    public void setOwner(Hibernate2Ex owner)
    {
        this.owner = owner;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

        Class<? extends Hibernate2NestEx> thisClass = this.getClass();
        Class<?> thatClass = obj.getClass();
        if (!thisClass.isAssignableFrom(thatClass) && !thatClass.isAssignableFrom(thisClass))
        {
            return false;
        }

        Hibernate2NestEx that = (Hibernate2NestEx) obj;

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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int reply = 7423;
        reply += (id == null) ? 382 : id.hashCode();
        reply += (name == null) ? 423 : name.hashCode();
        return reply;
    }

    private Integer id;

    private String name;

    private Hibernate2Ex owner;
}
