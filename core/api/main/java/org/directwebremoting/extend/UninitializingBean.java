package org.directwebremoting.extend;

/**
 * Like {@link InitializingBean} except that this requests notification when
 * things are shutting down.
 * @see InitializingBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface UninitializingBean
{
    /**
     * Called when {@link org.directwebremoting.Container#destroy()} is called
     * which usually happens when the DWR servlet is destroyed.
     */
    void destroy();
}
