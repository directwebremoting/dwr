package org.directwebremoting.impl;

import java.lang.reflect.Method;
import java.util.Map;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.CallbackHelper;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.IdGenerator;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.ParameterProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.RealRawData;
import org.directwebremoting.ui.Callback;

/**
 * The default implementation of CallbackHelper
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCallbackHelper implements CallbackHelper
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CallbackHelper#saveCallback(org.directwebremoting.proxy.Callback, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> String saveCallback(Callback<T> callback, Class<T> type)
    {
        String key = createUniqueId();

        for (ScriptSession session : callback.getScriptSessions())
        {
            // Save the callback itself
            Map<String, Callback<T>> callbackMap = (Map<String, Callback<T>>) session.getAttribute(KEY_CALLBACK);
            callbackMap.put(key, callback);
            session.setAttribute(KEY_CALLBACK, callbackMap);

            // And save the type of the callback
            Map<String, Class<T>> typeMap = (Map<String, Class<T>>) session.getAttribute(KEY_TYPE);
            typeMap.put(key, type);
            session.setAttribute(KEY_TYPE, typeMap);
        }

        return key;
    }

    /**
     * The reverse of {@link CallbackHelper#saveCallback(Callback, Class)}
     * which executes a {@link Callback} which has been called by the browser
     */
    @SuppressWarnings("unchecked")
    public static <T> void executeCallback(String key, RealRawData data) throws ConversionException
    {
        WebContext webContext = WebContextFactory.get();
        ScriptSession session = webContext.getScriptSession();
        ConverterManager converterManager = webContext.getContainer().getBean(ConverterManager.class);

        Map<String, Class<T>> typeMap = (Map<String, Class<T>>) session.getAttribute(KEY_TYPE);
        Class<T> type = typeMap.remove(key);
        session.removeAttribute(KEY_TYPE);
        session.setAttribute(KEY_TYPE, typeMap);

        try
        {
            Method method = Callback.class.getMethod("dataReturned", type);

            Property property = new ParameterProperty(new MethodDeclaration(method), 0);
            InboundVariable iv = data.getInboundVariable();
            Object callbackData  = converterManager.convertInbound(type, iv, property);

            Map<String, Callback<T>> callbackMap = (Map<String, Callback<T>>) session.getAttribute(KEY_TYPE);
            Callback<T> callback = callbackMap.remove(key);
            session.removeAttribute(KEY_TYPE);
            session.setAttribute(KEY_CALLBACK, callbackMap);

            callback.dataReturned((T) callbackData);
        }
        catch (Exception ex)
        {
            throw new ConversionException(type, ex);
        }
    }

    /**
     * Callbacks need a unique ID
     */
    public String createUniqueId()
    {
        return idGenerator.generate();
    }

    /**
     * The id generator
     */
    public void setIdGenerator(IdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    /**
     * The id generator
     */
    private IdGenerator idGenerator;

    /**
     * The key that we use in a script session to store Callbacks
     */
    public static final String KEY_CALLBACK = "org.directwebremoting.callback";

    /**
     * The key that we use in a script session to store Callbacks
     */
    public static final String KEY_TYPE = "org.directwebremoting.type";
}
