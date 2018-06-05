package org.directwebremoting.extend;

/**
 * Id generator for DWR's CSRF protection session cookies and other duties.
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public interface IdGenerator
{
    /**
     * Generates an id string unique enough to avoid duplicates within one
     * server session and all its corresponding user sessions.
     * @return A unique id string
     */
    String generate();
}
