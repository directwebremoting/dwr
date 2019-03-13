package org.directwebremoting.convert;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.ArrayOutboundVariable;
import org.directwebremoting.extend.ConvertUtil;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.ProtocolConstants;

/**
 * An implementation of Converter for Collections of Strings.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CollectionConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BaseV20Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    @Override
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    @SuppressWarnings("unchecked")
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        String value = data.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ProtocolConstants.INBOUND_ARRAY_START) || !value.endsWith(ProtocolConstants.INBOUND_ARRAY_END))
        {
            log.warn("Expected collection while converting data for " + paramType.getName() + " in " + data.getContext().getCurrentProperty() + ". Passed: " + value);
            throw new ConversionException(paramType, "Data conversion error. See logs for more details.");
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            Property parent = data.getContext().getCurrentProperty();
            Property child = parent.createChild(0);
            child = converterManager.checkOverride(child);
            Class<?> subtype = child.getPropertyType();

            // subtype.getMethod("h", null).getTypeParameters();
            Collection<Object> col;

            // If they want an iterator then just use an array list and fudge
            // at the end.
            if (Iterator.class.isAssignableFrom(paramType))
            {
                col = new ArrayList<Object>();
            }
            // If paramType is concrete then just use whatever we've got.
            else if (!paramType.isInterface() && !Modifier.isAbstract(paramType.getModifiers()))
            {
                // If there is a problem creating the type then we have no way
                // of completing this - they asked for a specific type and we
                // can't create that type. I don't know of a way of finding
                // subclasses that might be instaniable so we accept failure.
                //noinspection unchecked
                col = (Collection<Object>) paramType.newInstance();
            }
            // If they want a SortedSet then use TreeSet
            else if (SortedSet.class.isAssignableFrom(paramType))
            {
                col = new TreeSet<Object>();
            }
            // If they want a Set then use HashSet
            else if (Set.class.isAssignableFrom(paramType))
            {
                col = new HashSet<Object>();
            }
            // If they want a List then use an ArrayList
            else if (List.class.isAssignableFrom(paramType))
            {
                col = new ArrayList<Object>();
            }
            // If they just want a Collection then just use an ArrayList
            else if (Collection.class.isAssignableFrom(paramType))
            {
                col = new ArrayList<Object>();
            }
            else
            {
                throw new ConversionException(paramType);
            }

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            data.getContext().addConverted(data, paramType, col);

            StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_ARRAY_SEPARATOR);
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();

                String[] split = ConvertUtil.splitInbound(token);
                String splitType = split[ConvertUtil.INBOUND_INDEX_TYPE];
                String splitValue = split[ConvertUtil.INBOUND_INDEX_VALUE];

                InboundVariable nested = new InboundVariable(data.getContext(), null, splitType, splitValue);
                nested.dereference();

                Object output = converterManager.convertInbound(subtype, nested, child);
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
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(paramType, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    @SuppressWarnings("unchecked")
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        // First we need to get ourselves the collection data
        Iterator<Object> it;
        if (data instanceof Collection)
        {
            Collection<Object> col = (Collection<Object>) data;
            it = col.iterator();
        }
        else if (data instanceof Iterator)
        {
            it = (Iterator<Object>) data;
        }
        else
        {
            throw new ConversionException(data.getClass());
        }

        // Stash this bit of data to cope with recursion
        ArrayOutboundVariable ov = new ArrayOutboundVariable(outctx);
        outctx.put(data, ov);

        // Convert all the data members
        List<OutboundVariable> ovs = new ArrayList<OutboundVariable>();
        while (it.hasNext())
        {
            Object member = it.next();
            OutboundVariable nested;

            nested = converterManager.convertOutbound(member, outctx);

            ovs.add(nested);
        }

        // Group the list of converted objects into this OutboundVariable
        ov.setChildren(ovs);

        return ov;
    }

    /**
     * For nested conversions
     */
    private ConverterManager converterManager = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(CollectionConverter.class);
}
