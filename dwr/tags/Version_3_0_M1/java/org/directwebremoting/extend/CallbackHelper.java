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
package org.directwebremoting.extend;

import java.lang.reflect.Method;
import java.util.Map;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.io.RawData;
import org.directwebremoting.proxy.Callback;
import org.directwebremoting.util.IdGenerator;

/**
 * A class to help with the use of {@link Callback}s
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CallbackHelper
{
    /**
     * Used by the many proxy classes to record a callback for later calling
     * @param callback The callback that acts on the type
     * @param type The type of the data that we are recording
     * @return A key under which the data is stored
     */
    @SuppressWarnings("unchecked")
    public static <T> String saveCallback(Callback<T> callback, Class<T> type)
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
     * The reverse of {@link #saveCallback(Callback, Class)}
     * which executes a {@link Callback} which has been 
     * @param key
     * @param data
     * @throws MarshallException
     */
    @SuppressWarnings("unchecked")
    public static <T> void executeCallback(String key, RawData data) throws MarshallException
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

            InboundContext inctx = data.getInboundContext();
            TypeHintContext incc = new TypeHintContext(converterManager, method, 0);
            InboundVariable iv = data.getInboundVariable();
            Object callbackData  = converterManager.convertInbound(type, iv, inctx, incc);

            Map<String, Callback<T>> callbackMap = (Map<String, Callback<T>>) session.getAttribute(KEY_TYPE);
            Callback<T> callback = callbackMap.remove(key);
            session.removeAttribute(KEY_TYPE);
            session.setAttribute(KEY_CALLBACK, callbackMap);

            callback.dataReturned((T) callbackData);
        }
        catch (Exception ex)
        {
            throw new MarshallException(type, ex);
        }
    }

    /**
     * Callbacks need a unique ID
     */
    public static String createUniqueId()
    {
        return idGenerator.generateId(16);
    }

    /**
     * Callbacks need a unique ID
     */
    private static IdGenerator idGenerator = new IdGenerator();

    /**
     * The key that we use in a script session to store Callbacks
     */
    public static final String KEY_CALLBACK = "org.directwebremoting.callback";

    /**
     * The key that we use in a script session to store Callbacks
     */
    public static final String KEY_TYPE = "org.directwebremoting.type";
}
