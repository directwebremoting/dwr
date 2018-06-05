package org.directwebremoting.guice.util;

import java.lang.annotation.Annotation;

/**
 * @author Tim Peierls
 */
class NumberedImpl implements Numbered
{
    public NumberedImpl(long value)
    {
        this.value = value;
    }

    public long value()
    {
        return this.value;
    }

    public Class<? extends Annotation> annotationType()
    {
        return Numbered.class;
    }

    @Override
    public boolean equals(Object t)
    {
        if (!(t instanceof Numbered))
        {
            return false;
        }

        Numbered that = (Numbered) t;
        return this.value == that.value();
    }

    @Override
    public int hashCode()
    {
        // Annotation spec sez:
        return 127 * "value".hashCode() ^ (int)(value ^ (value >>> 32));
    }

    @Override
    public String toString()
    {
        return String.format("@%s(value=%d)", Numbered.class.getName(), value);
    }

    private final long value;
}
