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
package org.directwebremoting.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.Module;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.Loggers;

/**
 * An adapter module for Creators.
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public class CreatorModule implements Module
{
    public CreatorModule(Creator creator, AjaxFilterManager ajaxFilterManager, AccessControl accessControl, boolean allowImpossibleTests, String accessLogLevel, boolean debug)
    {
        this.creator = creator;
        this.ajaxFilterManager = ajaxFilterManager;
        this.accessControl = accessControl;
        this.allowImpossibleTests = allowImpossibleTests;
        this.accessLogLevel = accessLogLevel;
        this.debug = debug;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Module#getName()
     */
    public String getName()
    {
        return creator.getJavascript();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Module#isCacheable()
     */
    public boolean isCacheable()
    {
        return creator.isCacheable();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Module#getMethods()
     */
    public MethodDeclaration[] getMethods()
    {
        Method[] methods = creator.getType().getMethods();
        ArrayList<MethodDeclaration> methodDecls = new ArrayList<MethodDeclaration>();
        for (Method method : methods)
        {
            try
            {
                accessControl.assertMethodDisplayable(creator.getType(), method);
            }
            catch (SecurityException ex)
            {
                if (!allowImpossibleTests)
                {
                    continue;
                }
            }
            methodDecls.add(new MethodDeclaration(checkProxiedMethod(method)));
        }
        return methodDecls.toArray(new MethodDeclaration[0]);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Module#getMethod(java.lang.String, java.lang.Class<?>[])
     */
    public MethodDeclaration getMethod(String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException
    {
        Method method = checkProxiedMethod(creator.getType().getMethod(methodName, parameterTypes));
        return new MethodDeclaration(method);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Module#execute(org.directwebremoting.extend.MethodDeclaration, java.lang.Object[])
     */
    public Object executeMethod(MethodDeclaration methodDecl, Object[] parameters) throws Exception
    {
        // Get the actual method
        Method method = creator.getType().getMethod(methodDecl.getName(), methodDecl.getParameterTypes());

        accessControl.assertMethodExecutionIsPossible(creator.getType(), method);

        // Get ourselves an object to execute a method on unless the
        // method is static
        Object object = null;
        String scope = creator.getScope();
        boolean create = false;
        if (!Modifier.isStatic(method.getModifiers()))
        {
            WebContext webcx = WebContextFactory.get();

            // Check the various scopes to see if it is there
            if (scope.equals(Creator.APPLICATION))
            {
                object = webcx.getServletContext().getAttribute(getName());
            }
            else if (scope.equals(Creator.SESSION))
            {
                object = webcx.getSession().getAttribute(getName());
            }
            else if (scope.equals(Creator.SCRIPT))
            {
                object = webcx.getScriptSession().getAttribute(getName());
            }
            else if (scope.equals(Creator.REQUEST))
            {
                object = webcx.getHttpServletRequest().getAttribute(getName());
            }
            // Creator.PAGE scope means we create one every time anyway

            // If we don't have an object then call the creator
            try
            {
                if (object == null)
                {
                    create = true;
                    object = creator.getInstance();
                }
            }
            catch (InstantiationException ex)
            {
                // Allow Jetty RequestRetry exception to propagate to container
                Continuation.rethrowIfContinuation(ex);
                // We should log this regardless of the accessLogLevel.
                log.info("Error creating an instance of the following DWR Creator: " + ((null != creator.getClass()) ? creator.getClass().getName() : "None Specified") + ".", ex);
                throw ex;
            }

            // Remember it for next time
            if (create)
            {
                if (scope.equals(Creator.APPLICATION))
                {
                    // This might also be done at application startup by
                    // DefaultCreatorManager.addCreator(String, Creator)
                    webcx.getServletContext().setAttribute(getName(), object);
                }
                else if (scope.equals(Creator.SESSION))
                {
                    webcx.getSession().setAttribute(getName(), object);
                }
                else if (scope.equals(Creator.SCRIPT))
                {
                    webcx.getScriptSession().setAttribute(getName(), object);
                }
                else if (scope.equals(Creator.REQUEST))
                {
                    webcx.getHttpServletRequest().setAttribute(getName(), object);
                }
                // Creator.PAGE scope means we create one every time anyway
            }
        }

        // Log the call details if the accessLogLevel is call.
        if (AccessLogLevel.getValue(this.accessLogLevel, debug).hierarchy() == 0)
        {
            StringBuffer buffer = new StringBuffer();
            if (create)
            {
                buffer.append("Object created, ");
                if (!scope.equals(Creator.PAGE))
                {
                    buffer.append(" stored in ");
                    buffer.append(scope);
                }
                else
                {
                    buffer.append(" not stored");
                }
            }
            else
            {
                buffer.append("Object found in ");
                buffer.append(scope);
            }
            buffer.append(".");

            Loggers.ACCESS.info(buffer.toString());
        }

        // Execute the filter chain method.toString()
        List<AjaxFilter> filters = ajaxFilterManager.getAjaxFilters(getName());
        final Iterator<AjaxFilter> it = filters.iterator();
        AjaxFilterChain chain = new AjaxFilterChain()
        {
            public Object doFilter(Object obj, Method meth, Object[] params) throws Exception
            {
                if (it.hasNext())
                {
                    AjaxFilter next = it.next();
                    return next.doFilter(obj, meth, params, this);
                }
                else
                {
                    if ((null != obj && meth.getDeclaringClass().equals(obj.getClass())) || Modifier.isStatic(meth.getModifiers()))
                    {
                        return meth.invoke(obj, params);
                    }
                    else
                    {
                        // A proxied method
                        Method m = obj.getClass().getMethod(meth.getName(), meth.getParameterTypes());
                        return m.invoke(obj, params);
                    }
                }
            }
        };

        Object reply = chain.doFilter(object, method, parameters);
        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Module#toString()
     */
    @Override
    public String toString()
    {
        return creator.getClass().getSimpleName() + " for " + creator.getType().getName();
    }

    /**
     * The main issue here happens with JDK proxies, that is, those based on
     * interfaces and is easily noticeable with Spring because it's designed to
     * generate proxies on the fly for many different purposes (security, tx,..)
     * For some unknown reasons but probably related to erasure, when a proxy is
     * created and it contains a method with at least one generic parameter,
     * that generic type information is lost. Those that rely on reflection to
     * detect that info at runtime (our case when detecting the matching method
     * for an incoming call) face a dead end. The solution involves detecting
     * the proxy interface and obtain the original class (which holds the
     * required information).
     * <p>Here comes the problematic area. In the case of Spring all proxies
     * implement the Advised interface which includes a method that returns the
     * target class (and so fulfills our need). Of course, this means that:
     * a) A Spring dependency appears and
     * b) The solution only applies to Spring contexts.
     * The first concern is solvable using Class.forName. The current fix does
     * not solve the second. Probably a better solution should be implemented
     * (for example, something that works under the AOP alliance umbrella).
     */
    private Method checkProxiedMethod(Method method)
    {
        Method realMethod = method;
        if ((method != null) && (advisedClass != null) && advisedClass.isAssignableFrom(method.getDeclaringClass()))
        {
            if (Proxy.isProxyClass(creator.getType()))
            {
                try
                {
                    Object target = creator.getInstance(); // Should be a singleton
                    Method targetClassMethod = target.getClass().getMethod("getTargetClass");
                    Class<?> targetClass = (Class<?>) targetClassMethod.invoke(target);
                    realMethod = targetClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                }
                catch (Exception ex)
                {
                    // Probably not in Spring context so no Advised proxies at all
                }
            }
        }
        return realMethod;
    }

    /**
     * The Creator this class is a wrapper/adapter for
     */
    private final Creator creator;

    /**
     * What AjaxFilters apply to which Ajax calls?
     */
    private final AjaxFilterManager ajaxFilterManager;

    /**
     * Injected AccessControl
     */
    private final AccessControl accessControl;

    /**
     * Injected config param
     */
    private final boolean allowImpossibleTests;

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    protected final String accessLogLevel;

    /**
     * Are we in debug-mode and therefore more helpful at the expense of security?
     */
    private final boolean debug;

    /**
     * Spring/AOP hack
     * @see #checkProxiedMethod(Method)
     */
    private static Class<?> advisedClass;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(CreatorModule.class);

    /**
     * Spring/AOP hack
     * @see #checkProxiedMethod(Method)
     */
    static
    {
        try
        {
            advisedClass = Class.forName("org.springframework.aop.framework.Advised");
            log.debug("Found org.springframework.aop.framework.Advised enabling AOP checks");
        }
        catch (ClassNotFoundException ex)
        {
            log.debug("ClassNotFoundException on org.springframework.aop.framework.Advised skipping AOP checks");
        }
    }
}

