package org.directwebremoting.guice.util;

/**
 * Produces a Numbered instance for a given value.
 * @author Tim Peierls [tim at peierls dot net]
 */
public final class Numbers
{
    public static Numbered numbered(long n)
    {
        return new NumberedImpl(n);
    }
}
