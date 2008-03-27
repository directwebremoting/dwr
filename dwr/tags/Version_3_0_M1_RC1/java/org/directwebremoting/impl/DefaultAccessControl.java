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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AccessDeniedException;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.LoginRequiredException;
import org.directwebremoting.util.Messages;

/**
 * Control who should be accessing which methods on which classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultAccessControl implements AccessControl
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.AccessControl#assertExecutionIsPossible(org.directwebremoting.extend.Creator, java.lang.String, java.lang.reflect.Method)
     */
    public void assertExecutionIsPossible(Creator creator, String className, Method method) throws SecurityException
    {
        assertIsRestrictedByRole(className, method);
        assertIsDisplayable(creator, className, method);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.AccessControl#getReasonToNotDisplay(org.directwebremoting.Creator, java.lang.String, java.lang.reflect.Method)
     */
    public void assertIsDisplayable(Creator creator, String className, Method method) throws SecurityException
    {
        assertIsMethodPublic(method);
        assertIsExecutable(className, method.getName());
        assertIsNotOnBaseObject(method);

        if (!exposeInternals)
        {
            assertIsClassDwrInternal(creator);
            assertAreParametersDwrInternal(method);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.AccessControl#addRoleRestriction(java.lang.String, java.lang.String, java.lang.String)
     */
    public void addRoleRestriction(String scriptName, String methodName, String role)
    {
        String key = scriptName + '.' + methodName;
        Set<String> roles = roleRestrictMap.get(key);
        if (roles == null)
        {
            roles = new HashSet<String>();
            roleRestrictMap.put(key, roles);
        }

        roles.add(role);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.AccessControl#addIncludeRule(java.lang.String, java.lang.String)
     */
    public void addIncludeRule(String scriptName, String methodName)
    {
        Policy policy = getPolicy(scriptName);

        // If the policy for the given type is defaultAllow then we need to go
        // to default disallow mode, and check that the are not rules applied
        if (policy.defaultAllow)
        {
            if (!policy.rules.isEmpty())
            {
                throw new IllegalArgumentException(Messages.getString("DefaultAccessControl.MixedIncludesAndExcludes", scriptName));
            }

            policy.defaultAllow = false;
        }

        // Add the rule to this policy
        policy.rules.add(methodName);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.AccessControl#addExcludeRule(java.lang.String, java.lang.String)
     */
    public void addExcludeRule(String scriptName, String methodName)
    {
        Policy policy = getPolicy(scriptName);

        // If the policy for the given type is defaultAllow then we need to go
        // to default disallow mode, and check that the are not rules applied
        if (!policy.defaultAllow)
        {
            if (!policy.rules.isEmpty())
            {
                throw new IllegalArgumentException(Messages.getString("DefaultAccessControl.MixedIncludesAndExcludes", scriptName));
            }

            policy.defaultAllow = true;
        }

        // Add the rule to this policy
        policy.rules.add(methodName);
    }

    /**
     * @param scriptName The name of the creator to Javascript
     * @param method The method to execute
     */
    protected void assertIsRestrictedByRole(String scriptName, Method method)
    {
        String methodName = method.getName();

        // What if there is some J2EE role based restriction?
        Set<String> roles = getRoleRestrictions(scriptName, methodName);
        if (roles != null && !roles.isEmpty())
        {
            HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();

            assertAuthenticationIsValid(req);
            assertAllowedByRoles(req, roles);
        }
    }

    /**
     * @param scriptName The name of the creator to Javascript
     * @param methodName The name of the method (without brackets)
     * @return A Set of all the roles for the given script and method
     */
    protected Set<String> getRoleRestrictions(String scriptName, String methodName)
    {
        String key = scriptName + '.' + methodName;
        return roleRestrictMap.get(key);
    }

    /**
     * Check the users session for validity
     * @param req The users request
     * @throws SecurityException if the users session is invalid
     */
    protected static void assertAuthenticationIsValid(HttpServletRequest req) throws SecurityException
    {
        // ensure that at least the next call has a valid session
        req.getSession();

        // if there was an expired session, the request has to fail
        if (!req.isRequestedSessionIdValid())
        {
            throw new LoginRequiredException(Messages.getString("DefaultAccessControl.DeniedByInvalidSession"));
        }

        if (req.getRemoteUser() == null)
        {
            throw new LoginRequiredException(Messages.getString("DefaultAccessControl.DeniedByAuthenticationRequired"));
        }
    }

    /**
     * Is this current user in the given list of roles
     * @param req The users request
     * @param roles The list of roles to check
     * @throws SecurityException if this user is not allowed by the list of roles
     */
    protected static void assertAllowedByRoles(HttpServletRequest req, Set<String> roles) throws SecurityException
    {
        for (String role : roles)
        {
            if ("*".equals(role) || req.isUserInRole(role))
            {
                return;
            }
        }

        throw new AccessDeniedException(Messages.getString("DefaultAccessControl.DeniedByJ2EERoles"));
    }

    /**
     * Is the method public?
     * @param method The method that we wish to execute
     */
    protected static void assertIsMethodPublic(Method method)
    {
        if (!Modifier.isPublic(method.getModifiers()))
        {
            throw new SecurityException(Messages.getString("DefaultAccessControl.DeniedNonPublic"));
        }
    }

    /**
     * We ban some methods from {@link java.lang.Object}
     * @param method The method that should not be owned by {@link java.lang.Object}
     */
    protected static void assertIsNotOnBaseObject(Method method)
    {
        if (method.getDeclaringClass() == Object.class)
        {
            throw new SecurityException(Messages.getString("DefaultAccessControl.DeniedObjectMethod"));
        }
    }

    /**
     * Test to see if a method is excluded or included.
     * @param scriptName The name of the creator to Javascript
     * @param methodName The name of the method (without brackets)
     * @throws SecurityException if the method is allowed by the rules in addIncludeRule()
     * @see AccessControl#addIncludeRule(String, String)
     */
    protected void assertIsExecutable(String scriptName, String methodName) throws SecurityException
    {
        Policy policy = policyMap.get(scriptName);
        if (policy == null)
        {
            return;
        }

        // Find a match for this method in the policy rules
        String match = null;
        for (Iterator<String> it = policy.rules.iterator(); it.hasNext() && match == null;)
        {
            String test = it.next();

            // If at some point we wish to do regex matching on rules, here is
            // the place to do it.
            if (methodName.equals(test))
            {
                match = test;
            }
        }

        if (policy.defaultAllow && match != null)
        {
            // We are in default allow mode so the rules are exclusions and we
            // have a match, so this method is excluded.
            //log.debug("method excluded for creator " + type + " due to defaultAllow=" + policy.defaultAllow + " and rule: " + match);
            throw new SecurityException(Messages.getString("DefaultAccessControl.DeniedByAccessRules"));
        }

        // There may be a more optimized if statement here, but I value code
        // clarity over performance.
        //noinspection RedundantIfStatement

        if (!policy.defaultAllow && match == null)
        {
            // We are in default deny mode so the rules are inclusions and we
            // do not have a match, so this method is excluded.
            //log.debug("method excluded for creator " + type + " due to defaultAllow=" + policy.defaultAllow + " and rule: " + match);
            throw new SecurityException(Messages.getString("DefaultAccessControl.DeniedByAccessRules"));
        }
    }

    /**
     * Check the parameters are not DWR internal either
     * @param method The method that we want to execute
     */
    protected static void assertAreParametersDwrInternal(Method method)
    {
        for (int j = 0; j < method.getParameterTypes().length; j++)
        {
            Class<?> paramType = method.getParameterTypes()[j];

            // Access to org.directwebremoting is denied except for .io
            if (paramType.getName().startsWith(PACKAGE_DWR_DENY) && !paramType.getName().startsWith(PACKAGE_ALLOW_CONVERT))
            {
                throw new SecurityException(Messages.getString("DefaultAccessControl.DeniedParamDWR"));
            }
        }
    }

    /**
     * Is the class that we are executing a method on part of DWR?
     * @param creator The {@link Creator} that exposes the class
     */
    protected static void assertIsClassDwrInternal(Creator creator)
    {
        String name = creator.getType().getName();

        // Access to org.directwebremoting is denied except for .export
        if (name.startsWith(PACKAGE_DWR_DENY) && !name.startsWith(PACKAGE_ALLOW_CREATE))
        {
            throw new SecurityException(Messages.getString("DefaultAccessControl.DeniedCoreDWR"));
        }
    }

    /**
     * Find the policy for the given type and create one if none exists.
     * @param type The name of the creator
     * @return The policy for the given Creator
     */
    protected Policy getPolicy(String type)
    {
        Policy policy = policyMap.get(type);
        if (policy == null)
        {
            policy = new Policy();
            policyMap.put(type, policy);
        }

        return policy;
    }

    /**
     * @param exposeInternals the exposeInternals to set
     */
    public void setExposeInternals(boolean exposeInternals)
    {
        this.exposeInternals = exposeInternals;
    }

    /**
     * Do we allow DWR classes to be remoted?
     * @see #PACKAGE_DWR_DENY
     */
    protected boolean exposeInternals = false;

    /**
     * A map of Creators to policies
     */
    protected Map<String, Policy> policyMap = new HashMap<String, Policy>();

    /**
     * What role based restrictions are there?
     */
    protected Map<String, Set<String>> roleRestrictMap = new HashMap<String, Set<String>>();

    /**
     * A struct that contains a method access policy for a Creator
     */
    static class Policy
    {
        boolean defaultAllow = true;
        List<String> rules = new ArrayList<String>();
    }

    /**
     * My package name, so we can ban DWR classes from being created or marshalled
     */
    protected static final String PACKAGE_DWR_DENY = "org.directwebremoting.";

    /**
     * Special dwr package name from which classes may be created
     */
    protected static final String PACKAGE_ALLOW_CREATE = "org.directwebremoting.export.";

    /**
     * Special dwr package name from which classes may be converted
     */
    protected static final String PACKAGE_ALLOW_CONVERT = "org.directwebremoting.io.";
}
