package uk.ltd.getahead.dwr.impl;

import java.lang.reflect.*;
import java.util.*;

import org.directwebremoting.util.LocalUtil;

import uk.ltd.getahead.dwr.*;
import uk.ltd.getahead.dwr.util.*;


/**
 * SmartConverterManager extends the DefaultConverterManager
 * to allow the client to give a "hint" as to the actual
 * Java type that inbound variables should be converted to.
 * This converter manager searches objects for a special
 * property called '$javaClass' containing the name of a
 * convertable Java class.  If found, this type will be
 * used instead.
 *
 * <p>
 * Note that this converter manager does not change the
 * actual value of the inbound variable.  So, depending on
 * the converter implementation, you may see erroneous
 * errors reported for the '$javaClass' property.
 * </p>
 *
 * @author  Tim Dwelle [tim at dwelle dot net]
 *
 */
public class SmartConverterManager implements ConverterManager
{
	private static final String CANT_CONVERT_JAVA_CLASS =
	    "SmartConverterManager.CantConvertJavaClass";

	private static final String CLASS_NOT_FOUND =
	    "SmartConverterManager.ClassNotFound";

	private static final String JAVA_CLASS_PROPERTY =
	    "$javaClass";

	private static final String VALUE_PROPERTY =
	    "$value";

	private static final String WRAPPER_TOKEN =
	    SmartConverterManager.class.getName() + "$";

	private static final int WRAPPER_TOKEN_LEN =
	    WRAPPER_TOKEN.length();

    private static final Logger log =
        Logger.getLogger(SmartConverterManager.class);

	// this "has-a" relationship is way more pain than just
	// using inheritence, but hopefully this will make it
	// easier if in the future someone wants a smart
	// converter manager that wraps something other than
	// the default converter manager
	private ConverterManager inner;

	/**
	 * The default constructor creates an instance of
	 * SmartConverterManager that wraps the
	 * DefaultConverterManager implementation.
	 *
	 */
	public SmartConverterManager()
	{
		this(new DefaultConverterManager());
	}

	/**
	 * Creates an instance of SmartConverterManager that
	 * wraps around an arbitrary ConverterManager
	 * implementation to make it "smart".
	 *
	 */
	public SmartConverterManager(ConverterManager cm)
	{
		this.inner = cm;
	}

	/**
	 * Converts the inbound variable, using any provided
	 * '$javaClass' property as a "hint" of the actual Java
	 * type that inbound variable should be converted to.
	 *
	 */
	public Object convertInbound(Class paramType,
	                             InboundVariable iv,
	                             InboundContext inctx)
	    throws ConversionException
	{
		// look to see if we the inbound value
		// can even contain a $javaClass property
		String value = getValue(iv);

		if (value != null)
		{
			// see if we can find a $javaClass
			// property declared for this inbound
			// value
			Class c = getJavaClass(value, inctx);
			if (c != null)
			{
				// make sure the $javaClass returned
				// is still a suitable type for
				// conversion... and doesn't violate
				// the signature we are trying to match
				if (inner.isConvertable(c) &&
				    paramType.isAssignableFrom(c))
				{
					paramType = c;

					// we cant add our $javaClass property
					// to built-in js types (strings,
					// numbers, etc.) so we have to box
					// them into a wrapper...  so if we have
					// a wrapper, we need to get the value
					// out of it first
					if (isWrapper(value, inctx))
					{
						iv = getInboundVariable(value, VALUE_PROPERTY, inctx);
					}
				}
			}
		}

		return inner.convertInbound(paramType, iv, inctx);
	}

	/**
	 * isConvertable() does not really make sense, since any
	 * class may be a base class to a more specific,
	 * convertable class that is specified using the
	 * '$javaClass' syntax.
	 *
	 * <p>
	 * For now, this method will simply return true, and
	 * count on things to fail later, if the type is bad.
	 * </p>
	 *
	 */
	public boolean isConvertable(Class c)
	{
		return true;
	}

	/**
	 * Defers to the wrapped ConvererManager.
	 *
	 */
	public void addConverterType(String id, Class clazz)
	{
		inner.addConverterType(id, clazz);
	}

	/**
	 * Defers to the wrapped ConvererManager.
	 *
	 */
	public void addConverter(String match, String type, Map params)
	    throws IllegalArgumentException,
	           InstantiationException,
	           IllegalAccessException
	{
		inner.addConverter(match, type, params);
	}

	/**
	 * Defers to the wrapped ConvererManager.
	 *
	 */
	public void addConverter(String match, Converter converter)
	    throws IllegalArgumentException
	{
		inner.addConverter(match, converter);
	}

	/**
	 * Defers to the wrapped ConvererManager.
	 *
	 */
	public OutboundVariable convertOutbound(Object object,
	                                        OutboundContext converted)
	    throws ConversionException
	{
		return inner.convertOutbound(object, converted);
	}

	/**
	 * Defers to the wrapped ConvererManager.
	 *
	 */
	public void setExtraTypeInfo(Method method,
	                             int paramNo,
	                             int index,
	                             Class type)
	{
		inner.setExtraTypeInfo(method, paramNo, index, type);
	}

	/**
	 * Defers to the wrapped ConvererManager.
	 *
	 */
	public Class getExtraTypeInfo(Method method, int paramNo, int index)
	{
		return inner.getExtraTypeInfo(method, paramNo, index);
	}

	/**
	 * Defers to the wrapped ConvererManager.
	 *
	 */
	public void setConverters(Map converters)
	{
		inner.setConverters(converters);
	}

	/**
	 * Gets the inbound variable for the specified property
	 * of the provided value.
	 *
	 */
	private InboundVariable getInboundVariable(String value,
	                                           String property,
	                                           InboundContext inctx)
	{
		InboundVariable iv = null;

		StringTokenizer st = new StringTokenizer(value,
		    ConversionConstants.INBOUND_MAP_SEPARATOR);

		while (st.hasMoreTokens())
		{
			String token = st.nextToken();
			if (token.trim().length() == 0)
			{
				continue;
			}

			int colonpos = token.indexOf(
			    ConversionConstants.INBOUND_MAP_ENTRY);

			if (colonpos > -1)
			{
				String key = token.substring(0, colonpos).trim();
				String val = token.substring(colonpos + 1).trim();

				key = LocalUtil.decode(key);

				if (key.equals(property))
				{
                    String[] split = LocalUtil.splitInbound(val);
                    String type = split[LocalUtil.INBOUND_INDEX_TYPE];
                    String varVal = split[LocalUtil.INBOUND_INDEX_VALUE];

                    iv = new InboundVariable(inctx, type, varVal);
				}
			}
		}

		return iv;
	}

	/**
	 * Parse the properties for this value, looking for a
	 * '$javaClass' property.  If one exists, return the
	 * value as a Class.
	 *
	 */
	private Class getJavaClass(String value, InboundContext inctx)
	{
		Class c = null;

		InboundVariable classVar =
		   getInboundVariable(value, JAVA_CLASS_PROPERTY, inctx);

		if (classVar != null)
		{
			String className = null;

			try
			{
				Object obj = convertInbound(String.class, classVar, inctx);

				if (obj != null)
				{
					className = obj.toString();

					// the classname may be proceeded by
					// the wrapper token... if so, we want
					// to trim that part off
					if (className.indexOf(WRAPPER_TOKEN) == 0)
					{
						className = className.substring(WRAPPER_TOKEN_LEN);
					}

					c = LocalUtil.classForName(className);
				}
			}
			catch(ConversionException cex)
			{
				log.error(Messages.getString(CANT_CONVERT_JAVA_CLASS));
			}
			catch(ClassNotFoundException cnfex)
			{
				log.error(Messages.getString(CLASS_NOT_FOUND, className));
			}
        }

		return c;
	}

	/**
	 * Get the value from the inbound variable.  If the
	 * value is null, or not a map, then it can't possibly
	 * contain our '$javaClass' property... so null will be
	 * returned. Otherwise, the value will be returned, with
	 * the map's start & end trimmed off.
	 *
	 */
	private String getValue(InboundVariable iv)
	{
		String value = iv.getValue();

		if (value.trim().equals(ConversionConstants.INBOUND_NULL) ||
		    !value.startsWith(ConversionConstants.INBOUND_MAP_START) ||
		    !value.endsWith(ConversionConstants.INBOUND_MAP_END))
		{
			value = null;
		}
		else
		{
	        value = value.substring(1, value.length() - 1);
		}

		return value;
	}

	/**
	 * Checks to see whether or not the specified value is a
	 * wrapper for a non-object javascript type.
	 *
	 */
	private boolean isWrapper(String value, InboundContext inctx)
	{
		boolean wrapper = false;

		InboundVariable classVar =
		   getInboundVariable(value, JAVA_CLASS_PROPERTY, inctx);

		if (classVar != null)
		{
			try
			{
				Object obj = convertInbound(String.class, classVar, inctx);

				if (obj != null)
				{
					String className = obj.toString();

					// the classname may be proceeded by
					// the wrapper token... if so, we want
					// to trim that part off
					wrapper = (className.indexOf(WRAPPER_TOKEN) == 0);
				}
			}
			catch(ConversionException cex)
			{
				log.error(Messages.getString(CANT_CONVERT_JAVA_CLASS));
			}
        }

		return wrapper;
	}
}