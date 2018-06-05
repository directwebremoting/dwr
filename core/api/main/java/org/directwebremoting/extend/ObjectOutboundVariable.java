package org.directwebremoting.extend;

import java.util.Collection;
import java.util.Map;

import org.directwebremoting.util.LocalUtil;

/**
 * An OutboundVariable that declares a JavaScript object
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectOutboundVariable extends NestedOutboundVariable
{
    /**
     * Constructor for when we have a Javascript type to populate
     */
    public ObjectOutboundVariable(OutboundContext context, Class<?> type, String scriptClassName)
    {
        super(context);

        if (context.isJsonMode())
        {
            if (scriptClassName != null)
            {
                throw new JsonModeMarshallException(type, "Can't used named Javascript objects in JSON mode");
            }
        }

        this.scriptClassName = scriptClassName;
        this.isNamed = scriptClassName != null && scriptClassName.length() > 0;
    }

    /**
     * Constructor for when we don't have a Javascript type to populate
     */
    public ObjectOutboundVariable(OutboundContext context)
    {
        super(context);

        this.scriptClassName = null;
        this.isNamed = false;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NestedOutboundVariable#setChildren(java.util.Collection)
     */
    @Override
    public void setChildren(Collection<OutboundVariable> children)
    {
        throw new IllegalStateException("Use ObjectOutboundVariable.setChildren(Map<String, OutboundVariable>)");
    }

    /**
     * We setup the children later than construction time so we can check for
     * recursive references.
     */
    public void setChildren(Map<String, OutboundVariable> childMap)
    {
        this.childMap = childMap;
        super.setChildren(childMap.values());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NestedOutboundVariable#getDeclareCode()
     */
    public String getDeclareCode()
    {
        if (isInline())
        {
            return getChildDeclareCodes();
        }
        else
        {
            if (!isNamed)
            {
                return getChildDeclareCodes() + "var " + getVariableName() + "={};";
            }
            else
            {
                return getChildDeclareCodes() + "var " + getVariableName() + "=new " + scriptClassName + "();";
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getBuildCode()
     */
    public String getBuildCode()
    {
        if (isInline())
        {
            return getChildBuildCodes();
        }
        else
        {
            StringBuffer buildCode = new StringBuffer(getChildBuildCodes());

            String variableName = getVariableName();

            for (Map.Entry<String, OutboundVariable> entry : childMap.entrySet())
            {
                String name = entry.getKey();
                OutboundVariable nested = entry.getValue();

                String nestedAssignCode = nested.getAssignCode();

                // The semi-compact syntax is only any good for simple names
                if (LocalUtil.isSimpleName(name))
                {
                    buildCode.append(variableName);
                    buildCode.append('.');
                    buildCode.append(name);
                    buildCode.append('=');
                    buildCode.append(nestedAssignCode);
                    buildCode.append(';');
                }
                else
                {
                    buildCode.append(variableName);
                    buildCode.append("['");
                    buildCode.append(name);
                    buildCode.append("']=");
                    buildCode.append(nestedAssignCode);
                    buildCode.append(';');
                }
            }
            buildCode.append("\r\n");

            return buildCode.toString();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getAssignCode()
     */
    public String getAssignCode()
    {
        if (isInline())
        {
            StringBuffer buffer = new StringBuffer();

            if (isNamed)
            {
                buffer.append(EnginePrivate.remoteNewObjectFunction() + "(\"");
                buffer.append(scriptClassName);
                buffer.append("\",{");
            }
            else
            {
                buffer.append('{');
            }

            boolean first = true;
            for (Map.Entry<String, OutboundVariable> entry : childMap.entrySet())
            {
                String name = entry.getKey();
                OutboundVariable nested = entry.getValue();

                String innerAssignCode = nested.getAssignCode();

                if (!first)
                {
                    buffer.append(',');
                }

                // The compact syntax is only any good for simple names, when
                // we are not recursive, and when we're not doing JSON
                if (LocalUtil.isSimpleName(name) && !isJsonMode())
                {
                    buffer.append(name);
                    buffer.append(':');
                    buffer.append(innerAssignCode);
                }
                else
                {
                    buffer.append('\"');
                    buffer.append(name);
                    buffer.append("\":");
                    buffer.append(innerAssignCode);
                }

                // we don't need to do this one the hard way
                first = false;
            }

            if (isNamed)
            {
                buffer.append("})");
            }
            else
            {
                buffer.append('}');
            }

            return buffer.toString();
        }
        else
        {
            return getVariableName();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ObjectOutboundVariable:" + childMap;
    }

    /**
     * Are we named (or does {@link #scriptClassName} have some contents)
     */
    private final boolean isNamed;

    /**
     * The name of this typed class if there is one
     */
    private final String scriptClassName;

    /**
     * The contained variables
     */
    private Map<String, OutboundVariable> childMap;
}
