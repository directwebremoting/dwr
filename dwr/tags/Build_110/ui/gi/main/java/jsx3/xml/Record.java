package jsx3.xml;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Record class somewhat mirrors a W3C Element, but simplified because we do
 * not need namespaces, and things like Attr and NodeList.
 * It is also vaguely similar to the {@link List} interface in that it is a
 * container for attributes, however being also a container for other Records
 * the interface has some name changes and is greatly simplified.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class Record extends Node
{
	/**
	 * Ensure all Records have unique IDs
	 * @param id the jsxid for this record 
	 */
	public Record(String id)
	{
		super(id);
	}

	public Record(Object data)
	{
        introspectBean(data);
	}

    public Record(String id, Object data)
    {
        introspectBean(data);
        setId(id);
    }

    /**
     * @param data
     */
    private void introspectBean(Object data)
    {
        try
        {
            BeanInfo info = Introspector.getBeanInfo(data.getClass());
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

            for (PropertyDescriptor descriptor : descriptors)
            {
                String name = descriptor.getName();

                // We don't marshall getClass()
                if ("class".equals(name))
                {
                    continue;
                }

                Method getter = descriptor.getReadMethod();
                if (getter != null)
                {
                    Object reply = getter.invoke(data);
                    if (reply != null)
                    {
                        setAttribute(name, reply.toString());

                        if ("id".equals(name))
                        {
                            setId(reply.toString());
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }

	// Generic attribute management ////////////////////////////////////////////

	/**
     * Retrieves an attribute value by name.
     * @param name The name of the attribute to retrieve.
     * @return The attribute value as a string, or <code>null</code> if the
     * named attribute does not exist
     */
    public String getAttribute(String name)
    {
    	checkForJsxAttribute(name);
    	return attributes.get(name);
    }

    /**
     * Adds a new attribute. If an attribute with that name is already present 
     * in the element, its value is changed to be that of the value 
     * parameter.
     * @param name The name of the attribute to create or alter.
     * @param value Value to set in string form.
     */
    public Record setAttribute(String name, String value)
    {
    	checkForJsxAttribute(name);
    	attributes.put(name, value);
        return this;
    }

    /**
     * Removes an attribute by name.
     * <br>If no attribute with this name is found, this method has no effect.
     * @param name The name of the attribute to remove.
     * @return The old attribute value or null if it did not exist.
     */
    public String removeAttribute(String name)
    {
    	checkForJsxAttribute(name);
        return attributes.remove(name);
    }

    /**
     * Iterate over the names of the attributes (excluding the special JSX
     * attributes) contained in this {@link Record}.
     * @return an attribute name {@link Iterator}
     */
    public Iterator<String> getAttributeNames()
    {
        return attributes.keySet().iterator();
    }

	/**
	 * An internal check to keep JSX attributes separate from normal attributes
	 * @param name The name of the attribute to check that it does not start
	 * with '<code>jsx</code>'
	 */
	private void checkForJsxAttribute(String name)
	{
		if (name.startsWith("jsx"))
    	{
    		throw new IllegalArgumentException("Special JSX Attribute keys should be set directly");
    	}
	}

    private Map<String, String> attributes = new HashMap<String, String>();

    // Special JSX attribute management ////////////////////////////////////////

	/**
     * @return the disabled
     */
    public Boolean getDisabled()
    {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public Record setDisabled(Boolean disabled)
    {
        this.disabled = disabled;
        return this;
    }

    /**
     * @return the divider
     */
    public Boolean getDivider()
    {
        return divider;
    }

    /**
     * @param divider the divider to set
     */
    public Record setDivider(Boolean divider)
    {
        this.divider = divider;
        return this;
    }

    /**
     * @return the execute
     */
    public String getExecute()
    {
        return execute;
    }

    /**
     * @param execute the execute to set
     */
    public Record setExecute(String execute)
    {
        this.execute = execute;
        return this;
    }

    /**
     * @return the groupName
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public Record setGroupName(String groupName)
    {
        this.groupName = groupName;
        return this;
    }

    /**
     * @return the image
     */
    public String getImage()
    {
        return image;
    }

    /**
     * @param image the image to set
     */
    public Record setImage(String image)
    {
        this.image = image;
        return this;
    }

    /**
     * @return the keycodeString
     */
    public String getKeycodeString()
    {
        return keycodeString;
    }

    /**
     * @param keycodeString the keycodeString to set
     */
    public Record setKeycodeString(String keycodeString)
    {
        this.keycodeString = keycodeString;
        return this;
    }

    /**
     * @return the noMask
     */
    public String getNoMask()
    {
        return noMask;
    }

    /**
     * @param noMask the noMask to set
     */
    public Record setNoMask(String noMask)
    {
        this.noMask = noMask;
        return this;
    }

    /**
     * @return the selected
     */
    public Boolean getSelected()
    {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public Record setSelected(Boolean selected)
    {
        this.selected = selected;
        return this;
    }

    /**
     * @return the style
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * @param style the style to set
     */
    public Record setStyle(String style)
    {
        this.style = style;
        return this;
    }

    /**
     * @return the text
     */
    public String getText()
    {
        return text;
    }

    /**
     * @param text the text to set
     */
    public Record setText(String text)
    {
        this.text = text;
        return this;
    }

    /**
     * @return the tip
     */
    public String getTip()
    {
        return tip;
    }

    /**
     * @param tip the tip to set
     */
    public Record setTip(String tip)
    {
        this.tip = tip;
        return this;
    }

    /**
     * @return the unselectable
     */
    public Boolean getUnselectable()
    {
        return unselectable;
    }

    /**
     * @param unselectable the unselectable to set
     */
    public Record setUnselectable(Boolean unselectable)
    {
        this.unselectable = unselectable;
        return this;
    }

    // Support methods /////////////////////////////////////////////////////////

    /**
     * @param depth
     * @return The string version of this record
     */
    protected String toXml(int depth)
    {
        // Serialize the child records
        StringBuilder buffer = new StringBuilder();
        for (Record record : this)
        {
            buffer.append(Node.indent(depth));
            buffer.append(record.toXml(depth + 1));
            buffer.append("\n");
        }

        // Start the record tag
        StringBuilder reply = new StringBuilder();
        reply.append("<record jsxid=\"" + getId() + "\"");

        // Add the JSX attributes
        createAttributeOutput(reply, "jsxdisabled", disabled);
        createAttributeOutput(reply, "jsxdivider", divider);
        createAttributeOutput(reply, "jsxexecute", execute);
        createAttributeOutput(reply, "jsxgroupname", groupName);
        createAttributeOutput(reply, "jsximage", image);
        createAttributeOutput(reply, "jsxkeycode", keycodeString);
        createAttributeOutput(reply, "jsxnomask", noMask);
        createAttributeOutput(reply, "jsxselected", selected);
        createAttributeOutput(reply, "jsxstyle", style);
        createAttributeOutput(reply, "jsxtext", text);
        createAttributeOutput(reply, "jsxtip", tip);
        createAttributeOutput(reply, "jsxunselectable", unselectable);

        // Add the custom attributes
        for (Iterator<Map.Entry<String, String>> it = attributes.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<String, String> entry = it.next();
            createAttributeOutput(reply, entry.getKey(), entry.getValue());
        }

        if (buffer.length() == 0)
        {
            reply.append("/>\n");
        }
        else
        {
            reply.append(">\n");
            reply.append(buffer.toString());
            reply.append("</record>");
        }

        return reply.toString();
    }

    /**
     * @param reply
     */
    private void createAttributeOutput(StringBuilder reply, String name, Object value)
    {
            if (value != null)
            {
                    reply.append(" ");
                    reply.append(name);
                    reply.append("=\"");
                    reply.append(value);
                    reply.append("\"");
            }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
            return toXml(0);
    }

    private Boolean disabled = null;
    private Boolean divider = null;
    private String execute = null;
    private String groupName = null;
    private String image = null;
    private String keycodeString = null;
    private String noMask = null;
    private Boolean selected = null;
    private String style = null;
    private String text = null;
    private String tip = null;
    private Boolean unselectable = null;
}
