package org.directwebremoting.convert.mapped;

import java.util.HashSet;
import java.util.Set;

import org.directwebremoting.util.CompareUtil;

/**
 * A hibernate bean to fit the declaration in {@link org.directwebremoting.hibernate.Database}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Hibernate3Ex
{
    public Hibernate3Ex()
    {
    }

    public Hibernate3Ex(Integer id)
    {
        this.id = id;
    }

    public Hibernate3Ex(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Hibernate3Ex(Integer id, String name, Set<Hibernate3NestEx> children)
    {
        this.id = id;
        this.name = name;
        this.children = children;
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

    public Set<Hibernate3NestEx> getChildren()
    {
        return children;
    }

    public void setChildren(Set<Hibernate3NestEx> child)
    {
        this.children = child;
    }

    @Override
    public String toString()
    {
        return "Hibernate3Ex[id=" + getId() + ",name=" + getName() + ",children=" + getChildren().size() + "]";
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

        Class<? extends Hibernate3Ex> thisClass = this.getClass();
        Class<? extends Object> thatClass = obj.getClass();
        if (!thisClass.isAssignableFrom(thatClass) && !thatClass.isAssignableFrom(thisClass))
        {
            return false;
        }

        Hibernate3Ex that = (Hibernate3Ex) obj;

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

        if (!CompareUtil.equals(this.getChildren(), that.getChildren()))
        {
            return false;
        }

        return true;
    }

    private Integer id;

    private String name;

    private Set<Hibernate3NestEx> children = new HashSet<Hibernate3NestEx>();
}
