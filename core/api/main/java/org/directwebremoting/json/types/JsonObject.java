package org.directwebremoting.json.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.directwebremoting.util.JavascriptUtil;

/**
 * In official JSON parlance this should be called Object, however this would
 * cause confusion with {@link java.lang.Object} which is auto-imported.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonObject extends JsonValue implements Map<String, JsonValue>
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        StringBuffer output = new StringBuffer();
        output.append("{ ");

        boolean isFirst = true;
        for (Entry<String, JsonValue> entry : proxy.entrySet())
        {
            if (isFirst)
            {
                isFirst = false;
            }
            else
            {
                output.append(", ");
            }

            output.append('\'');
            output.append(JavascriptUtil.escapeJavaScript(entry.getKey(), false));
            output.append("':");
            output.append(entry.getValue().toExternalRepresentation());
        }
        output.append(" }");
        return output.toString();
    }

    /* (non-Javadoc)
     * @see java.util.Map#clear()
     */
    public void clear()
    {
        proxy.clear();
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key)
    {
        return proxy.containsKey(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value)
    {
        return proxy.containsValue(value);
    }

    /* (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    public Set<Entry<String, JsonValue>> entrySet()
    {
        return proxy.entrySet();
    }

    /* (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    public JsonValue get(Object key)
    {
        return proxy.get(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty()
    {
        return proxy.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    public Set<String> keySet()
    {
        return proxy.keySet();
    }

    /* (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public JsonValue put(String key, JsonValue value)
    {
        return proxy.put(key, value);
    }

    /* (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends String, ? extends JsonValue> otherMap)
    {
        proxy.putAll(otherMap);
    }

    /* (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    public JsonValue remove(Object key)
    {
        return proxy.remove(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#size()
     */
    public int size()
    {
        return proxy.size();
    }

    /* (non-Javadoc)
     * @see java.util.Map#values()
     */
    public Collection<JsonValue> values()
    {
        return proxy.values();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return toExternalRepresentation();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o)
    {
        return proxy.equals(o);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return proxy.hashCode();
    }

    /**
     * Where we store the values
     */
    private final Map<String, JsonValue> proxy = new HashMap<String, JsonValue>();
}
