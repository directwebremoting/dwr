package uk.ltd.getahead.dwr.convert;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import uk.ltd.getahead.dwr.ConversionData;
import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.ScriptSetup;

/**
 * An implementation of Converter for Collections of Strings.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class CollectionConverter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.Configuration)
     */
    public void init(ConverterManager newConfig)
    {
        this.config = newConfig;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertTo(java.lang.Class, uk.ltd.getahead.dwr.ConversionData, java.util.Map)
     */
    public Object convertTo(Class paramType, ConversionData data, Map working) throws ConversionException
    {
        String value = data.getValue();

        if (value.startsWith("["))
        {
            value = value.substring(1);
        }
        if (value.endsWith("]"))
        {
            value = value.substring(0, value.length() - 1);
        }

        try
        {
            StringTokenizer st = new StringTokenizer(value, ",");
            int size = st.countTokens();

            Collection col = null;

            // If they want an iterator then just use an array list and fudge
            // at the end.
            if (Iterator.class.isAssignableFrom(paramType))
            {
                col = new ArrayList();
            }
            // If paramType is concrete then just use whatever we've got.
            else if (!paramType.isInterface() && !Modifier.isAbstract(paramType.getModifiers()))
            {
                // If there is a problem creating the type then we have no way
                // of completing this - they asked for a specific type and we
                // can't create that type. I don't know of a way of finding
                // subclasses that might be instaniable so we accept failure.
                col = (Collection) paramType.newInstance();
            }
            // If they want a set then use HashSet
            else if (Set.class.isAssignableFrom(paramType))
            {
                col = new HashSet();
            }
            // If they want a list then use an ArrayList
            else if (List.class.isAssignableFrom(paramType))
            {
                col = new ArrayList();
            }
            // If they just want a collection then just use an ArrayList
            else if (Collection.class.isAssignableFrom(paramType))
            {
                col = new ArrayList();
            }
            else
            {
                throw new ConversionException("Can't convert javascript arrays to " + paramType);
            }

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            working.put(data, col);

            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();
                ConversionData nested = new ConversionData(data.getLookup(), token);
                Object output = config.convertTo(String.class, nested, working);
                col.add(output);
            }

            // If they wanted an Iterator then give them one otherwise use
            // the type we created
            if (Iterator.class.isAssignableFrom(paramType))
            {
                return col.iterator();
            }
            else
            {
                return col;
            }
        }
        catch (Exception ex)
        {
            throw new ConversionException(ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertFrom(java.lang.Object, java.lang.String, java.util.Map)
     */
    public String convertFrom(Object data, String varname, Map converted) throws ConversionException
    {
        Iterator it = null;
        if (data instanceof Collection)
        {
            Collection col = (Collection) data;
            it = col.iterator();
        }
        else if (data instanceof Iterator)
        {
            it = (Iterator) data;
        }
        else
        {
            throw new ConversionException("Can't convert " + data.getClass().getName() + " to a javscript array.");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("var " + varname + " = new Array();");

        int i = 0;
        while (it.hasNext())
        {
            Object element = it.next();

            ScriptSetup nested = config.convertFrom(element, converted);

            buffer.append(nested.initCode);
            buffer.append(varname);
            buffer.append("[");
            buffer.append(i);
            buffer.append("] = ");
            buffer.append(nested.assignCode);
            buffer.append(";");

            i++;
        }

        return buffer.toString();
    }

    /**
     * For nested conversions
     */
    private ConverterManager config;
}
