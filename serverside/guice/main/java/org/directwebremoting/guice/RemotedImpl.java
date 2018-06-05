package org.directwebremoting.guice;

import java.lang.annotation.Annotation;

/**
 * @author Tim Peierls
 */
class RemotedImpl implements Remoted
{
    public RemotedImpl()
    {
        this.value = "";
    }

    public RemotedImpl(String value)
    {
        if (value == null)
        {
            throw new NullPointerException("@Remoted");
        }
        this.value = value;
    }

    public String value()
    {
        return this.value;
    }

    public Class<? extends Annotation> annotationType()
    {
        return Remoted.class;
    }

    @Override
    public boolean equals(Object t)
    {
        if (!(t instanceof Remoted))
        {
            return false;
        }

        Remoted that = (Remoted) t;
        return this.value.equals(that.value());
    }

    @Override
    public int hashCode()
    {
        // Annotation spec sez:
        return 127 * "value".hashCode() ^ value.hashCode();
    }

    @Override
    public String toString()
    {
        return "@" + Remoted.class.getName() + "(value=" + value + ")";
    }

    private final String value;
}
