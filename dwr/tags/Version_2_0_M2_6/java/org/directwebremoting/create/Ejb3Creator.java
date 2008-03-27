package org.directwebremoting.create;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.directwebremoting.Creator;
import org.directwebremoting.util.Messages;

/**
 * 
 */
public class Ejb3Creator extends AbstractCreator implements Creator
{

    public final static String LOCAL = "local";

    //  remote interface by default
    boolean remote = true;

    String bean;

    String clazz;

    String beanend = "Bean";

    /**
     * The common interface of the Bean.
     * <b>If you don't have a common interface
     * from which local and remote are derived, you have to set the bean
     * name manually!</b>
     * The Beanname is gotten from the part of the String behind the last '.'
     * @param clazz The fully qualified classname of the Bean's interface
     *
     */
    public void setInterface(String clazz)
    {
        this.clazz = clazz;
        this.bean = clazz.substring(clazz.lastIndexOf(".") + 1);
    }

    /**
     * Set the name of the bean for the JNDI lookup
     * @param bean
     */
    public void setBean(String bean)
    {
        this.bean = bean;
    }

    /**
     * Get local or remote interface?
     * Defaults remote
     * @param interfazz Either "local" for the local interface or anything else for the remote
     */
    public void setInterfaceType(String interfazz)
    {
        if (interfazz.equalsIgnoreCase(LOCAL))
            remote = false;
        else
            remote = true;
    }

    /**
     * Set the ending that is appended to the actual bean name gotten from the
     * interface name (UserMgr --> UserMgrBean)
     *
     * defaults to "Bean"
     *  @param beanend
     */
    public void setBeanEnd(String beanend)
    {
        this.beanend = beanend;
    }

    public Class getType()
    {
        try
        {
            return Class.forName(clazz);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.BeanClassNotFound", clazz));
        }
    }

    public Object getInstance() throws InstantiationException
    {
        String type = remote ? "remote" : "local";
        try
        {
            Context jndi = getInitialContext();
            Object o = jndi.lookup(bean + beanend + "/" + type);
            return o;
        }
        catch (NamingException e)
        {
            throw new InstantiationException(bean + "/" + type + " not bound!");
        }
    }

    protected Context getInitialContext()
    {

        Properties props = new Properties();
        try
        {
            props.load(getClass().getResourceAsStream("/jndi.properties"));
            return new InitialContext(props);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
