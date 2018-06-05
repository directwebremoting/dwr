package org.directwebremoting.extend;

import org.directwebremoting.ConversionException;
import org.directwebremoting.util.LocalUtil;

/**
 * A simple struct to hold data about a single converted javascript variable.
 * An inbound variable will have either a value or a fileValue but not both.
 * If file is <code>true</code> fileValue will be populated, otherwise value
 * will be populated.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class InboundVariable
{
    /**
     * Parsing ctor
     * @param context How we lookup references
     * @param key The name of the variable that this was transfered as
     * @param type The type information from javascript
     * @param value The javascript variable converted to a string
     */
    public InboundVariable(InboundContext context, String key, String type, String value)
    {
        this(context, key, type, new FormField(value));
    }

    /**
     * Parsing ctor
     * @param context How we lookup references
     * @param key The name of the variable that this was transfered as
     * @param type The type information from javascript
     * @param value The javascript variable converted to a string
     * @param has the data already been URL decoded?
     */
    public InboundVariable(InboundContext context, String key, String type, String value, boolean urlDecoded)
    {
        this(context, key, type, new FormField(value), urlDecoded);
    }

    /**
     * Parsing ctor
     * @param context How we lookup references
     * @param key The name of the variable that this was transfered as
     * @param type The type information from javascript
     * @param fileField The javascript variable converted to a FormField
     */
    public InboundVariable(InboundContext context, String key, String type, FormField fileField)
    {
        this(context, key, type, fileField, false);
    }

    /**
     * Parsing ctor
     * @param context How we lookup references
     * @param key The name of the variable that this was transfered as
     * @param type The type information from javascript
     * @param fileField The javascript variable converted to a FormField
     * @param has the data already been URL decoded?
     */
    public InboundVariable(InboundContext context, String key, String type, FormField fileField, boolean urlDecoded)
    {
        this.context = context;
        this.key = key;
        this.type = type;
        this.members = null;
        this.formField = fileField;
        this.urlDecoded = urlDecoded;
    }

    /**
     * Constructor for when we need something to represent null
     * @param context How we lookup references
     */
    public InboundVariable(InboundContext context)
    {
        this.context = context;
        this.key = null;
        this.type = ProtocolConstants.INBOUND_NULL;
        this.formField = null;
        this.members = null;
        dereferenced = true;
    }

    /**
     * Constructor for when we need to temporarily create an InboundVariable
     * for sorting out varargs
     * @param context How we lookup references
     */
    public InboundVariable(InboundContext context, InboundVariable[] members)
    {
        this.context = context;
        this.key = null;
        this.type = ProtocolConstants.INBOUND_VARARGS;
        this.formField = null;
        this.members = members;
        dereferenced = true;
    }

    /**
     * Accessor of the context of the variable: the other related variables
     */
    public InboundContext getContext()
    {
        return context;
    }

    /**
     * Attempt to de-reference an inbound variable.
     * We try de-referencing as soon as possible (why? there is a good reason
     * for it, it fixes some bug, but I can't remember what right now) However
     * the referenced variable may not exist yet, so the de-referencing may
     * fail, requiring us to have another go later.
     * @throws ConversionException If cross-references don't add up
     */
    public void dereference() throws ConversionException
    {
        int depth = 0;

        while (ProtocolConstants.TYPE_REFERENCE.equals(type))
        {
            InboundVariable cd = context.getInboundVariable(formField.getString());
            if (cd == null)
            {
                throw new ConversionException(getClass(), "Found reference to variable named '" + formField.getString() + "', but no variable of that name could be found.");
            }

            type = cd.type;
            formField = cd.getFormField();
            key = cd.key;

            depth++;
            if (depth > 20)
            {
                throw new ConversionException(getClass(), "Max depth exceeded when dereferencing " + formField.getString());
            }
        }

        dereferenced = true;
    }

    public String urlDecode() {
        if (!urlDecoded) {
            return LocalUtil.urlDecode(getValue());
        }
        return getValue();
    }

    /**
     * If we are using object parameters that have specified types then the
     * {@link ConverterManager} will need to get to know what the required type
     * is.
     * @return The requested object type, or null if one was not specified
     */
    public String getNamedObjectType()
    {
        if (type.startsWith("Object_"))
        {
            return type.substring("Object_".length());
        }
        else
        {
            return null;
        }
    }

    /**
     * Was this type null on the way in
     * @return true if the javascript variable was null or undefined.
     */
    public boolean isNull()
    {
        return ProtocolConstants.INBOUND_NULL.equals(type);
    }

    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        return formField.getString();
    }

    /**
     * @return Returns the file value
     */
    public FormField getFormField()
    {
        return formField;
    }

    /**
     * @return Returns the JavaScript type.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Nasty hack to get around varargs
     */
    public InboundVariable[] getMembers()
    {
        return members;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (formField == null)
        {
            return type + ProtocolConstants.INBOUND_TYPE_SEPARATOR + "null";
        }

        return type + ProtocolConstants.INBOUND_TYPE_SEPARATOR + formField.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof InboundVariable))
        {
            return false;
        }

        InboundVariable that = (InboundVariable) obj;

        if (!dereferenced)
        {
            // We shouldn't really need to do this ...
            // dereference();
            throw new IllegalStateException("this.equals() called before dereference()");
        }

        if (!that.dereferenced)
        {
            // that.dereference();
            throw new IllegalStateException("that.equals() called before dereference()");
        }

        if (!this.type.equals(that.type))
        {
            return false;
        }

        if (!this.formField.equals(that.formField))
        {
            return false;
        }

        if (this.key == null || that.key == null)
        {
            return false;
        }

        return true; // this.key.equals(that.key);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        if (!dereferenced)
        {
            // dereference();
            throw new IllegalStateException("hashCode() called before dereference()");
        }

        int hash = 745;
        hash += (formField == null) ? 875 : formField.hashCode();
        hash += (type == null) ? 346 : type.hashCode();
        hash += (key == null) ? 768 : key.hashCode();
        return hash;
    }

    /**
     * Nasty hack to get around varargs
     */
    private final InboundVariable[] members;

    /**
     * Has the data been URL decoded?
     */
    private boolean urlDecoded;

    /**
     * It's an error to store this in a Map unless we have already called
     * {@link #dereference()}.
     */
    private boolean dereferenced = false;

    /**
     * How do be lookup references?
     */
    private final InboundContext context;

    /**
     * The variable name
     */
    private String key;

    /**
     * The javascript declared variable type
     */
    private String type;

    /**
     * The javascript declared file value
     */
    private FormField formField;
}
