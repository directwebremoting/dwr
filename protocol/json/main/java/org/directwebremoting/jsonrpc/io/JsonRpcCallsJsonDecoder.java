package org.directwebremoting.jsonrpc.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.Module;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.json.parse.impl.StatefulJsonDecoder;
import org.directwebremoting.util.JavascriptUtil;

import static javax.servlet.http.HttpServletResponse.*;

import static org.directwebremoting.jsonrpc.JsonRpcConstants.*;

/**
 * A JsonDecoder that creates a JsonRpcCalls structure.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcCallsJsonDecoder extends StatefulJsonDecoder
{
    /**
     * Force creators to give us the beans we need
     */
    public JsonRpcCallsJsonDecoder(ConverterManager converterManager, ModuleManager moduleManager)
    {
        this.converterManager = converterManager;
        this.moduleManager = moduleManager;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#addMemberToArray(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void addMemberToArray(Object parent, Object member) throws JsonParseException
    {
        if (!inParams)
        {
            log.error("JSON parse has addMemberToArray where inParms=false");
            calls.addParseError("Array not expected");
            return;
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
                    calls.addParseError("Expected property 'params' to be only object child");
                    return;
                }

                if (!(member instanceof List))
                {
                    log.error("JSON parse has addMemberToObject where member.class == " + member.getClass().getName() + " (expecting List) and propertyName=" + propertyName);
                    calls.addParseError("Expected property 'params' to be a list");
                    return;
                }

                params = (List<Object>) member;
                inParams = false;
                convertParams();
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
                calls.addParseError("Only params (and their children) can have children");
                return;
            }

            if ("params".equals(propertyName))
            {
                log.error("JSON parse has addMemberToObject where !inParams and propertyName=" + propertyName);
                calls.addParseError("Adding params property when not in params mode");
                return;
            }

            if ("id".equals(propertyName))
            {
                // id is allowed to be a string|number|boolean and we need to
                // recreate it exactly ...
                if (member instanceof String)
                {
                    calls.setBatchId(JavascriptUtil.escapeJavaScript((String) member));
                }
                else
                {
                    calls.setBatchId(member.toString());
                }
            }
            else
            {
                if (!(member instanceof String))
                {
                    log.error("JSON parse has addMemberToObject where member.class == " + member.getClass().getName() + " (expecting String) and propertyName=" + propertyName);
                    calls.addParseError("Expected string type");
                    return;
                }

                String data = (String) member;
                if ("jsonrpc".equals(propertyName))
                {
                    calls.setVersion(data);
                }
                else if ("method".equals(propertyName))
                {
                    String[] parts = data.split("\\.");
                    if (parts.length != 2)
                    {
                        log.warn("Got method='" + data + "', but this does not split into 2 parts (parts=" + Arrays.asList(parts) + ").");
                        calls.addParseError("method parameter did not split into 2 parts");
                        return;
                    }

                    scriptName = parts[0];
                    methodName = parts[1];
                }
            }
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
            calls.addParseError("json-rpc request must have a root object");
        }

        if (parent == calls)
        {
            if (!"params".equals(propertyName))
            {
                log.error("JSON parse has createArray where parent=request but propertyName=" + propertyName + " (expecting 'params')");
                calls.addParseError("Only params object in a request can be an array");
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
            calls.addParseError("There should be no child objects in a request object");
        }

        return new HashMap<String, Object>();
    }

    /**
     * We might not have enough information to convert the parameters because
     * the method parameter might not have been transfered, before the params
     * so we need to check for both
     */
    protected void convertParams()
    {
        if (scriptName == null || params == null)
        {
            return;
        }
        Module module = moduleManager.getModule(scriptName, false);
        if (module == null)
        {
            log.warn("No creator found: " + scriptName);
            throw new JsonRpcCallException(calls, "Object not valid", ERROR_CODE_INVALID, SC_BAD_REQUEST);
        }
        // Fill out the Calls structure
        try
        {
            // Get the types of the parameters
            List<Class<?>> paramTypes = new ArrayList<Class<?>>();
            for (Object param : params)
            {
                paramTypes.add(param.getClass());
            }
            Class<?>[] typeArray = paramTypes.toArray(new Class[paramTypes.size()]);

            MethodDeclaration method = module.getMethod(methodName, typeArray);

            Call call = new Call(null, scriptName, methodName);
            calls.addCall(call);
            call.setMethodDeclaration(method);
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

    /**
     * How we convert parameters
     */
    protected final ConverterManager converterManager;

    /**
     * How we create new beans
     */
    protected final ModuleManager moduleManager;

    /**
     *
     */
    private boolean inParams = false;

    /**
     *
     */
    private final JsonRpcCalls calls = new JsonRpcCalls();

    private String methodName;

    private String scriptName;

    /**
     *
     */
    private List<Object> params = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonRpcCallsJsonDecoder.class);
}
