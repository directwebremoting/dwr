/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.create;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;

/**
 * A Creator that works against EJB3 beans
 * @author Squishy [Squishy_I at gmx dot net]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Ejb3Creator extends AbstractCreator implements Creator
{
    /**
     * The common interface of the Bean.
     * <b>If you don't have a common interface from which local and remote are
     * derived, you have to set the bean name manually!</b>
     * The BeanName is fetched from the part of the String behind the last '.'
     * @param className The fully qualified class name of the Bean's interface
     */
    public void setInterface(String className)
    {
        this.className = className;
        this.bean = className.substring(className.lastIndexOf('.') + 1);
    }

    /**
     * Set the name of the bean for the JNDI lookup
     * @param bean The JNDI name to lookup
     */
    public void setBean(String bean)
    {
        this.bean = bean;
    }

    /**
     * Get local or remote interface?
     * Defaults remote
     * @param iface Either "local" for the local interface or anything else for the remote
     */
    public void setInterfaceType(String iface)
    {
        remote = !iface.equalsIgnoreCase(LOCAL);
    }

    /**
     * Set the ending that is appended to the actual bean name fetched from the
     * interface name (UserMgr --> UserMgrBean). Defaults to "Bean"
     * @param beanNamePostfix The name to append to a bean before lookup
     */
    public void setBeanNamePostfix(String beanNamePostfix)
    {
        this.beanNamePostfix = beanNamePostfix;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class<?> getType()
    {
        try
        {
            return LocalUtil.classForName(className);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("Class not found: " + className);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        String type = remote ? "remote" : "local";

        Context jndi = null;

        try
        {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/jndi.properties"));
            jndi = new InitialContext(props);

            return jndi.lookup(bean + beanNamePostfix + "/" + type);
        }
        catch (Exception ex)
        {
            throw new InstantiationException(bean + "/" + type + " not bound:" + ex.getMessage());
        }
        finally
        {
            if (jndi != null)
            {
                try
                {
                    jndi.close();
                }
                catch (NamingException ex)
                {
                    // Ignore
                }
            }
        }
    }

    /**
     * Constant for local/remote lookup
     */
    private static final String LOCAL = "local";

    /**
     * Are we using a remote interface? Default == true
     */
    private boolean remote = true;

    /**
     * The name of the bean
     */
    private String bean;

    /**
     * The type of the bean
     */
    private String className;

    /**
     * A suffix to help lookup
     */
    private String beanNamePostfix = "Bean";
}
