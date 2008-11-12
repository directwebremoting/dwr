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
package org.directwebremoting.jsonrpc.io;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.json.parse.impl.StatefulJsonDecoder;

import static javax.servlet.http.HttpServletResponse.*;

import static org.directwebremoting.jsonrpc.JsonRpcConstants.*;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcCallsJsonDecoder extends StatefulJsonDecoder
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#addMemberToArray(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void addMemberToArray(Object parent, Object member) throws JsonParseException
    {
        if (!inParams)
        {
            log.error("JSON parse has addMemberToArray where inParms=false");
            throw new IllegalStateException("Error parsing request");
        }

        @SuppressWarnings("unchecked")
        List<Object> array = (List<Object>) parent;
        array.add(member);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#addMemberToObject(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void addMemberToObject(Object parent, String propertyName, Object member) throws JsonParseException
    {
        if (inParams)
        {
            if (parent == calls)
            {
                if (!"params".equals(propertyName))
                {
                    log.error("JSON parse has addMemberToObject where parent=request and inParms=true for propertyName=" + propertyName);
                    throw new IllegalStateException("Error parsing request");
                }

                if (!(member instanceof List))
                {
                    log.error("JSON parse has addMemberToObject where member.class == " + member.getClass().getName() + " (expecting List) and propertyName=" + propertyName);
                    throw new IllegalStateException("Error parsing request");
                }

                params = (List<Object>) member;
                inParams = false;
                convertParamsIfPossible();
            }
            else
            {
                Map<String, Object> map = (Map<String, Object>) parent;
                map.put(propertyName, member);
            }
        }
        else
        {
            if (parent != calls)
            {
                log.error("JSON parse has addMemberToObject where parent != request and !inParms for propertyName=" + propertyName);
                throw new IllegalStateException("Error parsing request");
            }

            if ("params".equals(propertyName))
            {
                log.error("JSON parse has addMemberToObject where !inParams and propertyName=" + propertyName);
                throw new IllegalStateException("Error parsing request");
            }

            if (!(member instanceof String))
            {
                log.error("JSON parse has addMemberToObject where member.class == " + member.getClass().getName() + " (expecting String) and propertyName=" + propertyName);
                throw new IllegalStateException("Error parsing request");
            }

            String data = (String) member;
            if ("jsonrpc".equals(propertyName))
            {
                calls.setVersion(data);
            }
            else if ("method".equals(propertyName))
            {
                String[] parts = data.split(".");
                if (parts.length != 2)
                {
                    log.warn("Got method='" + data + "', but this does not split into 2 parts (parts=" + parts + ").");
                    throw new IllegalStateException("Error parsing request");
                }

                call.setScriptName(parts[0]);
                call.setMethodName(parts[1]);
                convertParamsIfPossible();
            }
            else if ("id".equals(propertyName))
            {
                calls.setBatchId(data);
            }
        }
    }

    /**
     * We might not have enough information to convert the parameters because
     * the method parameter might not have been transfered, before the params
     * so we need to check for both
     */
    protected void convertParamsIfPossible()
    {
        String methodName = call.getMethodName();
        String scriptName = call.getScriptName();
        if (scriptName == null || params == null)
        {
            return;
        }

        Creator creator = creatorManager.getCreator(scriptName, false);
        if (creator == null)
        {
            log.warn("No creator found: " + scriptName);
            throw new JsonRpcCallException(calls, "Object not valid", ERROR_CODE_INVALID, SC_BAD_REQUEST);
        }

        // Fill out the Calls structure
        try
        {
            Class<?> type = creator.getType();

            // Get the types of the parameters
            List<Class<?>> paramTypes = new ArrayList<Class<?>>();
            for (Object param : params)
            {
                paramTypes.add(param.getClass());
            }
            Class<?>[] typeArray = paramTypes.toArray(new Class[paramTypes.size()]);

            Method method = type.getMethod(call.getMethodName(), typeArray);
            call.setMethod(method);

            call.setParameters(params.toArray());
        }
        catch (SecurityException ex)
        {
            log.warn("Method not allowed: " + scriptName + "." + methodName, ex);
            throw new JsonRpcCallException(calls, "Method not allowed", ERROR_CODE_INVALID, SC_BAD_REQUEST);
        }
        catch (NoSuchMethodException ex)
        {
            log.warn("Method not allowed: " + scriptName + "." + methodName, ex);
            throw new JsonRpcCallException(calls, "Method not found", ERROR_CODE_INVALID, SC_BAD_REQUEST);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#createArray(java.lang.Object, java.lang.String)
     */
    @Override
    protected Object createArray(Object parent, String propertyName) throws JsonParseException
    {
        if (parent == null)
        {
            log.error("JSON parse has createArray where parent=null");
            throw new IllegalStateException("Error parsing request");
        }

        if (parent == calls)
        {
            if (!"params".equals(propertyName))
            {
                log.error("JSON parse has createArray where parent=request but propertyName=" + propertyName + " (expecting 'params')");
                throw new IllegalStateException("Error parsing request");
            }

            inParams = true;
        }

        return new ArrayList<Object>();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#createObject(java.lang.Object, java.lang.String)
     */
    @Override
    protected Object createObject(Object parent, String propertyName) throws JsonParseException
    {
        if (parent == null)
        {
            return calls;
        }

        if (parent == calls)
        {
            log.error("JSON parse has createObject where parent=request and propertyName=" + propertyName);
            throw new IllegalStateException("Error parsing request");
        }

        return new HashMap<String, Object>();
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param converterManager The new DefaultConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param creatorManager The new DefaultConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
    }

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     *
     */
    private boolean inParams = false;

    /**
     *
     */
    private final JsonRpcCalls calls = new JsonRpcCalls();

    /**
     *
     */
    private final Call call = new Call();

    /**
     *
     */
    private List<Object> params = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonRpcCallsJsonDecoder.class);
}
