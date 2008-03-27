package uk.ltd.getahead.dwr.impl;

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

import uk.ltd.getahead.dwr.AccessControl;
import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.Messages;

/**
 * Control who should be accessing which methods on which classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultAccessControl implements AccessControl
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AccessControl#getReasonToNotExecute(javax.servlet.http.HttpServletRequest, uk.ltd.getahead.dwr.Creator, java.lang.String, java.lang.reflect.Method)
     */
    public String getReasonToNotExecute(HttpServletRequest req, Creator creator, String className, Method method)
    {
        String methodName = method.getName();

        // What if there is some J2EE role based restriction?
        Set roles = getRoleRestrictions(className, methodName);
        if (roles != null)
        {
            boolean allowed = false;

            for (Iterator it = roles.iterator(); it.hasNext() && !allowed;)
            {
                String role = (String) it.next();
                if (req.isUserInRole(role))
                {
                    allowed = true;
                }
            }

            if (!allowed)
            {
                // This just creates a list of allowed roles for better debugging
                StringBuffer buffer = new StringBuffer();
                for (Iterator it = roles.iterator(); it.hasNext();)
                {
                    String role = (String) it.next();
                    buffer.append(role);
                    if (it.hasNext())
                    {
                        buffer.append(", "); //$NON-NLS-1$
                    }
                }

                return Messages.getString("ExecuteQuery.DeniedByJ2EERoles", buffer.toString()); //$NON-NLS-1$
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AccessControl#getReasonToNotDisplay(javax.servlet.http.HttpServletRequest, uk.ltd.getahead.dwr.Creator, java.lang.String, java.lang.reflect.Method)
     */
    public String getReasonToNotDisplay(HttpServletRequest req, Creator creator, String className, Method method)
    {
        String methodName = method.getName();

        // Is it public
        if (!Modifier.isPublic(method.getModifiers()))
        {
            return Messages.getString("ExecuteQuery.DeniedNonPublic"); //$NON-NLS-1$
        }

        // Do access controls allow it?
        if (!isExecutable(className, methodName))
        {
            return Messages.getString("ExecuteQuery.DeniedByAccessRules"); //$NON-NLS-1$
        }

        // Is it disallowed because it is part of DWR?
        if (creator.getType().getName().startsWith(PACKAGE_DWR))
        {
            return Messages.getString("ExecuteQuery.DeniedCoreDWR"); //$NON-NLS-1$
        }

        // Check the parameters are not DWR internal either
        for (int j = 0; j < method.getParameterTypes().length; j++)
        {
            Class paramType = method.getParameterTypes()[j];

            // Is it access to this type disallowed because it is part of DWR?
            if (paramType.getName().startsWith(PACKAGE_DWR))
            {
                return Messages.getString("ExecuteQuery.DeniedParamDWR"); //$NON-NLS-1$
            }
        }

        // We ban some methods from Object too
        if (method.getDeclaringClass() == Object.class)
        {
            return Messages.getString("ExecuteQuery.DeniedObjectMethod"); //$NON-NLS-1$
        }

        return null;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AccessControl#addRoleRestriction(java.lang.String, java.lang.String, java.lang.String)
     */
    public void addRoleRestriction(String scriptName, String methodName, String role)
    {
        String key = scriptName + '.' + methodName;
        Set roles = (Set) roleRestrictMap.get(key);
        if (roles == null)
        {
            roles = new HashSet();
            roleRestrictMap.put(key, roles);
        }

        roles.add(role);
    }

    /**
     * @param scriptName The name of the creator to Javascript
     * @param methodName The name of the method (without brackets)
     * @return A Set of all the roles for the given script and method
     */
    private Set getRoleRestrictions(String scriptName, String methodName)
    {
        String key = scriptName + '.' + methodName;
        return (Set) roleRestrictMap.get(key);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AccessControl#addIncludeRule(java.lang.String, java.lang.String)
     */
    public void addIncludeRule(String scriptName, String methodName)
    {
        Policy policy = getPolicy(scriptName);

        // If the policy for the given type is defaultAllow then we need to go
        // to default disallow mode, and check that the are not rules applied
        if (policy.defaultAllow == true)
        {
            if (policy.rules.size() > 0)
            {
                throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.MixedIncludesAndExcludes", scriptName)); //$NON-NLS-1$
            }

            policy.defaultAllow = false;
        }

        // Add the rule to this policy
        policy.rules.add(methodName);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AccessControl#addExcludeRule(java.lang.String, java.lang.String)
     */
    public void addExcludeRule(String scriptName, String methodName)
    {
        Policy policy = getPolicy(scriptName);

        // If the policy for the given type is defaultAllow then we need to go
        // to default disallow mode, and check that the are not rules applied
        if (policy.defaultAllow == false)
        {
            if (policy.rules.size() > 0)
            {
                throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.MixedIncludesAndExcludes", scriptName)); //$NON-NLS-1$
            }

            policy.defaultAllow = true;
        }

        // Add the rule to this policy
        policy.rules.add(methodName);
    }

    /**
     * Test to see if a method is excluded or included.
     * @param scriptName The name of the creator to Javascript
     * @param methodName The name of the method (without brackets)
     * @return true if the method is allowed by the rules in addIncludeRule()
     * @see AccessControl#addIncludeRule(String, String)
     */
    private boolean isExecutable(String scriptName, String methodName)
    {
        Policy policy = (Policy) policyMap.get(scriptName);
        if (policy == null)
        {
            return true;
        }

        // Find a match for this method in the policy rules
        String match = null;
        for (Iterator it = policy.rules.iterator(); it.hasNext() && match == null;)
        {
            String test = (String) it.next();

            // If at some point we wish to do regex matching on rules, here is
            // the place to do it.
            if (methodName.equals(test))
            {
                match = test;
            }
        }

        // There may be a more optimized if statement here, but I value code
        // clarity over performance.

        if (policy.defaultAllow && match != null)
        {
            // We are in default allow mode so the rules are exclusions and we
            // have a match, so this method is excluded.
            //log.debug("method excluded for creator " + type + " due to defaultAllow=" + policy.defaultAllow + " and rule: " + match); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return false;
        }

        if (!policy.defaultAllow && match == null)
        {
            // We are in default deny mode so the rules are inclusions and we
            // do not have a match, so this method is excluded.
            //log.debug("method excluded for creator " + type + " due to defaultAllow=" + policy.defaultAllow + " and rule: " + match); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return false;
        }

        return true;
    }

    /**
     * Find the policy for the given type and create one if none exists.
     * @param type The name of the creator
     * @return The policy for the given Creator
     */
    private Policy getPolicy(String type)
    {
        Policy policy = (Policy) policyMap.get(type);
        if (policy == null)
        {
            policy = new Policy();
            policyMap.put(type, policy);
        }

        return policy;
    }

    /**
     * A map of Creators to policies
     */
    private Map policyMap = new HashMap();

    /**
     * What role based restrictions are there?
     */
    private Map roleRestrictMap = new HashMap();

    /**
     * A struct that contains a method access policy for a Creator
     */
    static class Policy
    {
        boolean defaultAllow = true;
        List rules = new ArrayList();
    }

    /**
     * My package name, so we can ban DWR classes from being created or marshalled
     */
    private static final String PACKAGE_DWR = "uk.ltd.getahead.dwr."; //$NON-NLS-1$
}
