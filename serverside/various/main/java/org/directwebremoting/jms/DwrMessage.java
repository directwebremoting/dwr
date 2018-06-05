package org.directwebremoting.jms;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Hub;
import org.directwebremoting.event.MessageEvent;

/**
 * An implementation of all the {@link Message} types rolled into one.
 * This allows DWR to not know what type of Message the user wishes to work with
 * and to make the decision at runtime depending on how they choose to cast us.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrMessage implements Message, MapMessage, TextMessage, ObjectMessage
{
    /**
     * Default ctor
     */
    public DwrMessage()
    {
    }

    /**
     * Ctor for setting up a {@link TextMessage}
     */
    public DwrMessage(String text)
    {
        setText(text);
    }

    /**
     * Ctor for setting up an {@link ObjectMessage}
     */
    public DwrMessage(Serializable object)
    {
        setObject(object);
    }

    /**
     * Ctor for propagation from the DWR {@link Hub}.
     */
    public DwrMessage(Hub hub, MessageEvent message)
    {
        setMessageEvent(hub, message);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#acknowledge()
     */
    public void acknowledge()
    {
        throw Unsupported.noManualAcknowledgment();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#clearBody()
     */
    public void clearBody()
    {
        throw new IllegalStateException("Can raw JMS messages have bodies?");
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#clearProperties()
     */
    public void clearProperties()
    {
        properties.clear();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getPropertyNames()
     */
    public Enumeration<String> getPropertyNames()
    {
        return Collections.enumeration(properties.keySet());
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#propertyExists(java.lang.String)
     */
    public boolean propertyExists(String name)
    {
        return properties.containsKey(name);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getBooleanProperty(java.lang.String)
     */
    public boolean getBooleanProperty(String name)
    {
        return Boolean.parseBoolean(getStringProperty(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getByteProperty(java.lang.String)
     */
    public byte getByteProperty(String name)
    {
        return Byte.parseByte(getStringProperty(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getDoubleProperty(java.lang.String)
     */
    public double getDoubleProperty(String name)
    {
        return Double.parseDouble(getStringProperty(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getFloatProperty(java.lang.String)
     */
    public float getFloatProperty(String name)
    {
        return Float.parseFloat(getStringProperty(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getIntProperty(java.lang.String)
     */
    public int getIntProperty(String name)
    {
        return Integer.parseInt(getStringProperty(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getLongProperty(java.lang.String)
     */
    public long getLongProperty(String name)
    {
        return Long.parseLong(getStringProperty(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getObjectProperty(java.lang.String)
     */
    public Object getObjectProperty(String name)
    {
        return properties.get(name);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getShortProperty(java.lang.String)
     */
    public short getShortProperty(String name)
    {
        return Short.parseShort(getStringProperty(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getStringProperty(java.lang.String)
     */
    public String getStringProperty(String name)
    {
        return properties.get(name).toString();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setBooleanProperty(java.lang.String, boolean)
     */
    public void setBooleanProperty(String name, boolean value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setByteProperty(java.lang.String, byte)
     */
    public void setByteProperty(String name, byte value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setDoubleProperty(java.lang.String, double)
     */
    public void setDoubleProperty(String name, double value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setFloatProperty(java.lang.String, float)
     */
    public void setFloatProperty(String name, float value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setIntProperty(java.lang.String, int)
     */
    public void setIntProperty(String name, int value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setLongProperty(java.lang.String, long)
     */
    public void setLongProperty(String name, long value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setObjectProperty(java.lang.String, java.lang.Object)
     */
    public void setObjectProperty(String name, Object value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setShortProperty(java.lang.String, short)
     */
    public void setShortProperty(String name, short value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setStringProperty(java.lang.String, java.lang.String)
     */
    public void setStringProperty(String name, String value)
    {
        properties.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSCorrelationID()
     */
    public String getJMSCorrelationID()
    {
        return correlationId;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
     */
    public byte[] getJMSCorrelationIDAsBytes()
    {
        return correlationId.getBytes();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSDeliveryMode()
     */
    public int getJMSDeliveryMode()
    {
        return deliveryMode;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSDestination()
     */
    public Destination getJMSDestination()
    {
        return destination;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSExpiration()
     */
    public long getJMSExpiration()
    {
        return expiration;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSMessageID()
     */
    public String getJMSMessageID()
    {
        return messageId;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSPriority()
     */
    public int getJMSPriority()
    {
        return priority;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSRedelivered()
     */
    public boolean getJMSRedelivered()
    {
        return redelivered;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSReplyTo()
     */
    public Destination getJMSReplyTo()
    {
        return replyTo;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSTimestamp()
     */
    public long getJMSTimestamp()
    {
        return timestamp;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#getJMSType()
     */
    public String getJMSType()
    {
        return type;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSCorrelationID(java.lang.String)
     */
    public void setJMSCorrelationID(String correlationID)
    {
        this.correlationId = correlationID;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])
     */
    public void setJMSCorrelationIDAsBytes(byte[] correlationID)
    {
        this.correlationId = new String(correlationID);
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSDeliveryMode(int)
     */
    public void setJMSDeliveryMode(int deliveryMode)
    {
        this.deliveryMode = deliveryMode;
        throw Unsupported.noTransactions();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSDestination(javax.jms.Destination)
     */
    public void setJMSDestination(Destination destination)
    {
        this.destination = destination;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSExpiration(long)
     */
    public void setJMSExpiration(long expiration)
    {
        this.expiration = expiration;
        throw Unsupported.noMessageExpiry();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSMessageID(java.lang.String)
     */
    public void setJMSMessageID(String messageId)
    {
        this.messageId = messageId;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSPriority(int)
     */
    public void setJMSPriority(int priority)
    {
        this.priority = priority;
        throw Unsupported.noMessagePriority();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSRedelivered(boolean)
     */
    public void setJMSRedelivered(boolean redelivered)
    {
        this.redelivered = redelivered;
        throw Unsupported.noTransactions();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSReplyTo(javax.jms.Destination)
     */
    public void setJMSReplyTo(Destination replyTo)
    {
        this.replyTo = replyTo;
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSTimestamp(long)
     */
    public void setJMSTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    /* (non-Javadoc)
     * @see javax.jms.Message#setJMSType(java.lang.String)
     */
    public void setJMSType(String type)
    {
        this.type = type;
    }

    /**
     * Holds a reference to a message that we are replying/referring to
     */
    private String correlationId;

    /**
     * @see DeliveryMode
     */
    private int deliveryMode;

    /**
     * The topic or queue that we are destined for
     */
    private Destination destination;

    /**
     * How long until this message expires
     */
    private long expiration;

    /**
     * All JMS messages need a unique id
     */
    private String messageId;

    /**
     * @see Message#setJMSPriority(int)
     */
    private int priority;

    /**
     * Perhaps we will support this when we support Gears?
     */
    private boolean redelivered;

    /**
     * The topic or queue that message replies should be sent to
     */
    private Destination replyTo;

    /**
     * @see Message#setJMSTimestamp(long)
     */
    private long timestamp;

    /**
     * @see Message#setJMSType(String)
     */
    private String type;

    /**
     * The hash of properties assigned to this message
     */
    private Map<String, Object> properties = new HashMap<String, Object>();

    // The methods from MapMessage 

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getMapNames()
     */
    public Enumeration<String> getMapNames()
    {
        return Collections.enumeration(map.keySet());
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#itemExists(java.lang.String)
     */
    public boolean itemExists(String name)
    {
        return map.containsKey(name);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String name)
    {
        return Boolean.parseBoolean(getString(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getByte(java.lang.String)
     */
    public byte getByte(String name)
    {
        return Byte.parseByte(getString(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getBytes(java.lang.String)
     */
    public byte[] getBytes(String name)
    {
        return getString(name).getBytes();
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getChar(java.lang.String)
     */
    public char getChar(String name)
    {
        return getString(name).charAt(0);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getDouble(java.lang.String)
     */
    public double getDouble(String name)
    {
        return Double.parseDouble(getString(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getFloat(java.lang.String)
     */
    public float getFloat(String name)
    {
        return Float.parseFloat(getString(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getInt(java.lang.String)
     */
    public int getInt(String name)
    {
        return Integer.parseInt(getString(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getLong(java.lang.String)
     */
    public long getLong(String name)
    {
        return Long.parseLong(getString(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getShort(java.lang.String)
     */
    public short getShort(String name)
    {
        return Short.parseShort(getString(name));
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getString(java.lang.String)
     */
    public String getString(String name)
    {
        return getObject(name).toString();
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#getObject(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Object getObject(String name)
    {
        switch (source)
        {
        case MAP:
            return map.get(name);

        case MESSAGE_EVENT:
            return message.getData(Map.class);

        case NONE:
            return "";

        case SERIALIZABLE:
            return ((Map<String, Object>) object).get(name);

        case TEXT:
            return "";

        default:
            return "";
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean(String name, boolean value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setByte(java.lang.String, byte)
     */
    public void setByte(String name, byte value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setBytes(java.lang.String, byte[])
     */
    public void setBytes(String name, byte[] value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setBytes(java.lang.String, byte[], int, int)
     */
    public void setBytes(String name, byte[] value, int offset, int length)
    {
        byte[] data = new byte[length];
        System.arraycopy(data, 0, value, offset, length);
        map.put(name, data);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setChar(java.lang.String, char)
     */
    public void setChar(String name, char value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setDouble(java.lang.String, double)
     */
    public void setDouble(String name, double value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setFloat(java.lang.String, float)
     */
    public void setFloat(String name, float value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setInt(java.lang.String, int)
     */
    public void setInt(String name, int value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setLong(java.lang.String, long)
     */
    public void setLong(String name, long value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setObject(java.lang.String, java.lang.Object)
     */
    public void setObject(String name, Object value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setShort(java.lang.String, short)
     */
    public void setShort(String name, short value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    /* (non-Javadoc)
     * @see javax.jms.MapMessage#setString(java.lang.String, java.lang.String)
     */
    public void setString(String name, String value)
    {
        map.put(name, value);
        setSource(Source.MAP);
    }

    // The methods from TextMessage

    /* (non-Javadoc)
     * @see javax.jms.TextMessage#getText()
     */
    public String getText()
    {
        switch (source)
        {
        case MAP:
            return null;

        case MESSAGE_EVENT:
            return message.getData(String.class);

        case NONE:
            return null;

        case SERIALIZABLE:
            return object.toString();

        case TEXT:
            return text;

        default:
            return null;
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.TextMessage#setText(java.lang.String)
     */
    public void setText(String text)
    {
        this.text = text;
        setSource(Source.TEXT);
    }

    // The methods from ObjectMessage

    /* (non-Javadoc)
     * @see javax.jms.ObjectMessage#getObject()
     */
    public Serializable getObject()
    {
        switch (source)
        {
        case MAP:
            return null;

        case MESSAGE_EVENT:
            return (Serializable) message.getRawData();

        case NONE:
            return null;

        case SERIALIZABLE:
            return object;

        case TEXT:
            return text;

        default:
            return null;
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.ObjectMessage#setObject(java.io.Serializable)
     */
    public void setObject(Serializable object)
    {
        this.object = object;
        setSource(Source.SERIALIZABLE);
    }

    /**
     * @param hub
     * @param message
     */
    private void setMessageEvent(Hub hub, MessageEvent message)
    {
        this.hub = hub;
        this.message = message;
        setSource(Source.MESSAGE_EVENT);
    }

    /**
     * We might want to warn people about gratuitous source type changes
     */
    private void setSource(Source source)
    {
        if (this.source != null && this.source != source)
        {
            log.warn("Changing source of message from " + this.source + " to " + source);
        }

        this.source = source;
    }

    /**
     * Where did the data for this message come from?
     */
    enum Source
    {
        /**
         * Use by the DWR Hub. Data is stored in hub and message
         * @see #hub
         * @see #message
         */
        MESSAGE_EVENT,

        /**
         * There is no data, we have to assume headers only
         */
        NONE,

        /**
         * Data is stored in text
         * @see #text
         */
        TEXT,

        /**
         * Data is stored in the map
         * @see #map
         */
        MAP,

        /**
         * Data is stored in the serializable object
         * @see #object
         */
        SERIALIZABLE,

        /**
         * Streamed data is not supported yet
         */
        // STREAM,

        /**
         * Binary data is not supported yet
         */
        // BYTES,
    }

    /**
     * What is the current source of our data
     */
    protected Source source = null;

    /**
     * The object that we are wrapping
     */
    protected Serializable object;

    /**
     * The message passed over JMS
     */
    protected String text;

    /**
     * The map of contained objects
     */
    protected Map<String, Object> map = new HashMap<String, Object>();

    /**
     * The Hub for when data is received from the client
     */
    protected Hub hub;

    /**
     * The message straight out of the hub
     */
    protected MessageEvent message;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrMessage.class);
}
