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
package uk.ltd.getahead.dwr.create;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * This is a DWR creator implementation, to allow dwr beans to be allocated into
 * JSF scopes and into jeffs3 specific scope (i.e. the flow scope)
 * @author Pierpaolo Follia (Latest revision: $Author: esa50833 $)
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsfCreator extends AbstractCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getType()
     */
    public Class getType()
    {
        if (instanceType == null)
        {
            try
            {
                instanceType = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                log.error("Failed to instansiate object to detect type.", ex); //$NON-NLS-1$
                return Object.class;
            }
        }

        return instanceType;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null)
        {
            log.error("Object " + getManagedBeanName() + " cannot be created since the faces context is null"); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }

        Application application = facesContext.getApplication();
        Object resolvedObject = null;

        if (isVBExpression(getManagedBeanName()))
        {
            ValueBinding vb = application.createValueBinding(getManagedBeanName());
            if (vb != null)
            {
                resolvedObject = vb.getValue(facesContext);
            }
        }
        else
        {
            VariableResolver resolver = application.getVariableResolver();
            resolvedObject = resolver.resolveVariable(facesContext, getManagedBeanName());
        }

        return resolvedObject;
    }

    /**
     * Determine whether String is a value binding expression or not.
     * @param expression The expression to test for value bindingness
     * @return true if the expression contains a VB expression
     */
    public static boolean isVBExpression(String expression)
    {
        if (expression == null)
        {
            return false;
        }

        int start = expression.indexOf("#{"); //$NON-NLS-1$
        int end = expression.indexOf('}');

        return start != -1 && start < end;
    }

    /**
     * @return Returns the managedBeanName.
     */
    public String getManagedBeanName()
    {
        return managedBeanName;
    }

    /**
     * @param managedBeanName The managedBeanName to set.
     */
    public void setManagedBeanName(String managedBeanName)
    {
        this.managedBeanName = managedBeanName;
    }

    /**
     * What sort of class do we create?
     * @param classname The name of the class
     */
    public void setClass(String classname)
    {
        try
        {
            this.instanceType = Class.forName(classname);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("Creator.ClassNotFound"); //$NON-NLS-1$
        }
    }

    /**
     * The name of the bean to get from the FacesContext
     */
    private String managedBeanName;

    /**
     * The cached type of bean that we are creating
     */
    private Class instanceType;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(JsfCreator.class);
}
