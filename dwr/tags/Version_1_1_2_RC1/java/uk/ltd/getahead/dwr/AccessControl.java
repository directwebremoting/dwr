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
package uk.ltd.getahead.dwr;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

/**
 * Control who should be accessing which methods on which classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface AccessControl
{
    /**
     * Check the method for accessibility at runtime, and return an error
     * message if anything is wrong. If nothing is wrong, return null.
     * <p>See notes on <code>getReasonToNotDisplay()</code>. This method should
     * duplicate the tests made by this method.
     * <p>This is not a great becuase it mixes 2 bits of information in the same
     * variable (is it wrong, and what is wrong) but without multi-value returns
     * in Java this seems like the most simple implementation.
     * @param req The request from which we work out roles
     * @param creator Where does the method come from?
     * @param className The Javascript name of the class
     * @param method What is the method to execute?
     * @return null if nothing is wrong or an error message
     * @see AccessControl#getReasonToNotDisplay(HttpServletRequest, Creator, String, Method)
     */
    String getReasonToNotExecute(HttpServletRequest req, Creator creator, String className, Method method);

    /**
     * Check the method for accessibility at 'compile-time' (i.e. when the app
     * is downloaded), and return an error message if anything is wrong. If
     * nothing is wrong, return null.
     * <p>This method is similar to <code>getReasonToNotExecute()</code> except
     * that there may be checks (like security checks) that we wish to make only
     * at runtime in case the situation changes between 'compile-time' and
     * runtime.
     * <p>This is not a great becuase it mixes 2 bits of information in the same
     * variable (is it wrong, and what is wrong) but without multi-value returns
     * in Java this seems like the most simple implementation.
     * @param req The request from which we work out roles
     * @param creator Where does the method come from?
     * @param className The Javascript name of the class
     * @param method What is the method to execute?
     * @return null if nothing is wrong or an error message
     * @see AccessControl#getReasonToNotExecute(HttpServletRequest, Creator, String, Method)
     */
    String getReasonToNotDisplay(HttpServletRequest req, Creator creator, String className, Method method);

    /**
     * J2EE role based security allows us to restrict methods to only being used
     * by people in certain roles.
     * @param scriptName The name of the creator to Javascript
     * @param methodName The name of the method (without brackets)
     * @param role The new role name to add to the list for the given scriptName and methodName
     */
    void addRoleRestriction(String scriptName, String methodName, String role);

    /**
     * Add an include rule.
     * Each creator can have either a list of inclusions or a list of exclusions
     * but not both. If a creator has a list of inclusions then the default
     * policy is to deny any method that is not specifically included. If the
     * creator has a list of exclusions then the default policy is to allow
     * any method not listed.
     * If there are no included or excluded rules then the default policy is to
     * allow all methods
     * @param scriptName The name of the creator to Javascript
     * @param methodName The name of the method (without brackets)
     */
    void addIncludeRule(String scriptName, String methodName);

    /**
     * Add an exclude rule.
     * @param scriptName The name of the creator to Javascript
     * @param methodName The name of the method (without brackets)
     * @see AccessControl#addIncludeRule(String, String)
     */
    void addExcludeRule(String scriptName, String methodName);
}
