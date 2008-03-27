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
        if (facesContext != null)
        {
            log.error("Object " + getManagedBeanName() + " cannot be created since the faces context is null"); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }

        Application application = facesContext.getApplication();

        VariableResolver resolver = application.getVariableResolver();
        Object resolvedObject = resolver.resolveVariable(facesContext, getManagedBeanName());

        return resolvedObject;
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

    /**
     * If we need reflection based access then we can do something like this
     *
    public Object getInstance() throws InstantiationException
    {
        try
        {
            // FacesContext facesContext = FacesContext.getCurrentInstance();
            Object facesContext = getCurrentInstanceMethod.invoke(null, new Object[0]);
    
            if (facesContext != null)
            {
                log.error("Object " + getManagedBeanName() + " cannot be created since the faces context is null"); //$NON-NLS-1$ //$NON-NLS-2$
                return null;
            }
    
            // Application application = facesContext.getApplication();
            Object application = getApplicationMethod.invoke(facesContext, new Object[0]);
    
            // VariableResolver resolver = application.getVariableResolver();
            Object resolver = getVariableResolverMethod.invoke(application, new Object[0]);
    
            // Object resolvedObject = resolver.resolveVariable(facesContext, getManagedBeanName());
            Object resolvedObject = resolveVariableMethod.invoke(resolver, new Object[] { facesContext, getManagedBeanName() });
    
            return resolvedObject;
        }
        catch (InvocationTargetException ex)
        {
            Throwable target = ex.getTargetException();
            throw new InstantiationException(target.toString());
        }
        catch (Exception ex)
        {
            throw new InstantiationException(ex.toString());
        }
    }

    static
    {
        try
        {
            Class facesContextClass = Class.forName("javax.faces.context.FacesContext"); //$NON-NLS-1$
            getCurrentInstanceMethod = facesContextClass.getMethod("getCurrentInstance", null); //$NON-NLS-1$
            getApplicationMethod = facesContextClass.getMethod("getApplication", null); //$NON-NLS-1$

            Class applicationClass = Class.forName("javax.faces.application.Application"); //$NON-NLS-1$
            getVariableResolverMethod = applicationClass.getMethod("getVariableResolver", null); //$NON-NLS-1$

            Class variableResolver = Class.forName("javax.faces.el.VariableResolver"); //$NON-NLS-1$
            resolveVariableMethod = variableResolver.getMethod("resolveVariable", null); //$NON-NLS-1$
        }
        catch (ClassNotFoundException ex)
        {
            throw new NoClassDefFoundError(ex.getMessage());
        }
        catch (SecurityException ex)
        {
            throw new IncompatibleClassChangeError(ex.getMessage());
        }
        catch (NoSuchMethodException ex)
        {
            throw new IncompatibleClassChangeError(ex.getMessage());
        }
    }

    private static Method getCurrentInstanceMethod = null;
    private static Method getApplicationMethod = null;
    private static Method getVariableResolverMethod = null;
    private static Method resolveVariableMethod = null;
    */
}
