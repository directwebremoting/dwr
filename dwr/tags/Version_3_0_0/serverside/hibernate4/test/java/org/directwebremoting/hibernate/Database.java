package org.directwebremoting.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * DB abstraction code to help Hibernate tests
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Database
{
    /**
     * The commands we execute to create and populate a database
     */
    private static final String[] STARTUP =
    {
        "CREATE TABLE parent (id INT PRIMARY KEY, name VARCHAR(50));",
        "CREATE TABLE child (id INT PRIMARY KEY, name VARCHAR(50), owner INT);",
        "ALTER TABLE child ADD CONSTRAINT child_owner_fk FOREIGN KEY (owner) REFERENCES parent (id);",
        "INSERT INTO parent (id, name) VALUES (1, 'fred');",
        "INSERT INTO child (id, name, owner) VALUES (2, 'jim', 1);",
    };

    /**
     * Create a new in-memory database using HSQLDB
     */
    public static synchronized void init(Session session)
    {
        if (inited)
        {
            return;
        }

        try
        {
            for (String sql : STARTUP)
            {
                log.info("Exec: " + sql);
                System.out.println("Exec: " + sql);
                session.createSQLQuery(sql).executeUpdate();
            }
        }
        catch (Exception ex)
        {
            log.warn("Failed to start hsqldb", ex);
        }
        finally
        {
            inited = true;
        }
    }

    /**
     * Has init() been called?
     */
    private static boolean inited = false;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Database.class);
}
