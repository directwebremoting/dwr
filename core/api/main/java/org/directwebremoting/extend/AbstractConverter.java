package org.directwebremoting.extend;


/**
 * A way to migrate from the DWRv2.0 Converter style to something in the future.
 * Inheriting from this is advised to help with forwards compatibility.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    public void setConverterManager(ConverterManager converterManager)
    {
    }
}
