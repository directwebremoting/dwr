package org.directwebremoting.io;

/**
 * A wrapper around a {@link String} to distinguish a string entered into a
 * {@link org.directwebremoting.ScriptBuffer} as code and a string entered as
 * data.
 * <p>The difference comes down to escaping rules: A string passed as data must
 * be escaped. A script passed as code must not be escaped. It is very unlikely
 * that this class will be of any use to users of DWR, however it is used by
 * ScriptBuffer, and DWR's security rules forbid conversion of DWR defined
 * objects unless they are in this package.
 */
public class StringWrapper
{
    /**
     * All StringWrappers must have a string to wrap 
     */
    public StringWrapper(String data)
    {
        this.data = data;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return data;
    }

    private final String data;
}
