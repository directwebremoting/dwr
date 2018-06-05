package org.directwebremoting.json.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonArray extends JsonValue implements List<JsonValue>
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        StringBuffer output = new StringBuffer();
        output.append("[ ");

        boolean isFirst = true;
        for (JsonValue value : proxy)
        {
            if (isFirst)
            {
                isFirst = false;
            }
            else
            {
                output.append(", ");
            }

            output.append(value.toExternalRepresentation());
        }
        output.append(" ]");
        return output.toString();
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

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, JsonValue value)
    {
        proxy.add(index, value);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(JsonValue value)
    {
        return proxy.add(value);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends JsonValue> collection)
    {
        return proxy.addAll(collection);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection<? extends JsonValue> collection)
    {
        return proxy.addAll(index, collection);
    }

    /* (non-Javadoc)
     * @see java.util.List#clear()
     */
    public void clear()
    {
        proxy.clear();
    }

    /* (non-Javadoc)
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object sought)
    {
        return proxy.contains(sought);
    }

    /* (non-Javadoc)
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> collection)
    {
        return proxy.containsAll(collection);
    }

    /* (non-Javadoc)
     * @see java.util.List#get(int)
     */
    public JsonValue get(int index)
    {
        return proxy.get(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object sought)
    {
        return proxy.indexOf(sought);
    }

    /* (non-Javadoc)
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty()
    {
        return proxy.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.List#iterator()
     */
    public Iterator<JsonValue> iterator()
    {
        return proxy.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object sought)
    {
        return proxy.lastIndexOf(sought);
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator()
     */
    public ListIterator<JsonValue> listIterator()
    {
        return proxy.listIterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<JsonValue> listIterator(int startIndex)
    {
        return proxy.listIterator(startIndex);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    public JsonValue remove(int value)
    {
        return proxy.remove(value);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object value)
    {
        return proxy.remove(value);
    }

    /* (non-Javadoc)
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> collection)
    {
        return proxy.removeAll(collection);
    }

    /* (non-Javadoc)
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> collection)
    {
        return proxy.retainAll(collection);
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    public JsonValue set(int index, JsonValue value)
    {
        return proxy.set(index, value);
    }

    /* (non-Javadoc)
     * @see java.util.List#size()
     */
    public int size()
    {
        return proxy.size();
    }

    /* (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */
    public List<JsonValue> subList(int startIndex, int endIndex)
    {
        return proxy.subList(startIndex, endIndex);
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray()
     */
    public Object[] toArray()
    {
        return proxy.toArray();
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray(T[])
     */
    public <T> T[] toArray(T[] toFill)
    {
        return proxy.toArray(toFill);
    }

    private final List<JsonValue> proxy = new ArrayList<JsonValue>();
}
