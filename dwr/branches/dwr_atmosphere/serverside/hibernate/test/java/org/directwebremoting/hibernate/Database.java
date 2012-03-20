package org.directwebremoting.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.util.LocalUtil;

/**
 * Database abstraction code to help Hibernate tests
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
    public static synchronized void init()
    {
        if (inited)
        {
            return;
        }

        Connection con = null;
        Statement stmt = null;

        try
        {
            LocalUtil.classForName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:mem:dwr-test", "sa", "");
            stmt = con.createStatement();
            for (String sql : STARTUP)
            {
                stmt.execute(sql);
            }
        }
        catch (Exception ex)
        {
            log.warn("Failed to start hsqldb", ex);
        }
        finally
        {
            try
            {
                if (con != null)
                {
                    con.close();
                }
                if (stmt != null)
                {
                    stmt.close();
                }
            }
            catch (SQLException ex)
            {
                log.warn("Exception on close", ex);
            }

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
