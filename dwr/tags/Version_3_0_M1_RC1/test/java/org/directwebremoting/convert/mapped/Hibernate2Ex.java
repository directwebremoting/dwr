package org.directwebremoting.convert.mapped;

import java.util.HashSet;
import java.util.Set;

import org.directwebremoting.util.CompareUtil;

/**
 * A hibernate bean to fit the declaration in {@link org.directwebremoting.hibernate.Database}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Hibernate2Ex
{
    public Hibernate2Ex()
    {
    }

    public Hibernate2Ex(Integer id)
    {
        this.id = id;
    }

    public Hibernate2Ex(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Hibernate2Ex(Integer id, String name, Set<Hibernate2NestEx> children)
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

    public Set<Hibernate2NestEx> getChildren()
    {
        return children;
    }

    public void setChildren(Set<Hibernate2NestEx> child)
    {
        this.children = child;
    }

    @Override
    public String toString()
    {
        return "Hibernate2Ex[id=" + getId() + ",name=" + getName() + ",children=" + getChildren().size() + "]";
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

        Class<? extends Hibernate2Ex> thisClass = this.getClass();
        Class<? extends Object> thatClass = obj.getClass();
        if (!thisClass.isAssignableFrom(thatClass) && !thatClass.isAssignableFrom(thisClass))
        {
            return false;
        }

        Hibernate2Ex that = (Hibernate2Ex) obj;

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

    private Set<Hibernate2NestEx> children = new HashSet<Hibernate2NestEx>();
}
