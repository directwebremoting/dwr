package org.directwebremoting.fluent;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.Container;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.impl.SignatureParser;
import org.directwebremoting.util.Logger;

/**
 * A {@link Configurator} that used the FluentInterface style as
 * <a href="http://www.martinfowler.com/bliki/FluentInterface.html">described by
 * Martin Fowler</a>.
 * 
 * <p>To wire up the configuration programatically rather than having to use
 * <code>dwr.xml</code>. In order to use this style, you'll need to:</p>
 * 
 * <ul>
 * <li>Create a concrete implementation of {@link FluentConfigurator} which
 * implements the {@link #configure()} method.</li>
 * <li>Add an init param '<code>customConfigurator</code>' to the DWR servlet in
 * <code>web.xml</code> to point at your new class.</li>
 * </ul>
 * 
 * <p>The implementation of {@link #configure()} will look something like
 * this:</p>
 * 
 * <pre>
 * public void configure() {
 *    withConverterType("dog", "com.yourcompany.beans.Dog");
 *    withCreatorType("ejb", "com.yourcompany.dwr.creator.EJBCreator");
 *    withCreator("new", "ApartmentDAO")
 *        .addParam("scope", "session")
 *        .addParam("class", "com.yourcompany.dao.ApartmentDAO")
 *        .exclude("saveApartment")
 *        .withAuth("method", "role");
 *    withCreator("struts", "DogDAO")
 *        .addParam("clas", "com.yourcompany.dao.DogDAO")
 *        .include("getDog")
 *        .include("getColor");
 *    withConverter("dog", "*.Dog")
 *        .addParam("name", "value");
 *    withSignature()
 *        .addLine("import java.util.List;")
 *        .addLine("import com.example.Check;")
 *        .addLine("Check.setLotteryResults(List<Integer> nos);");
 * }
 * </pre>
 * @author Aaron Johnson [ajohnson at cephas dot net / <a href="http://cephas.net/blog">http://cephas.net/blog</a>]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class FluentConfigurator implements Configurator
{
    /**
     * This method is used to configure DWR using the fluent style.
     */
    public abstract void configure();

    /**
     * Add a new {@link Converter} definition.
     * @param id The id referred to by the {@link #withConverter(String, String)}
     * @param className The implementation of {@link Converter} to instansitate.
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator withConverterType(String id, String className)
    {
        setState(STATE_INIT_CONVERT);
        converterManager.addConverterType(id, className);
        return this;
    }

    /**
     * Use a {@link Converter} to instansiate a class
     * @param newConverter A predefined {@link Converter} or one defined by
     * {@link #withConverterType(String, String)}.
     * @param newMatch The javascript name of this component
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator withConverter(String newConverter, String newMatch)
    {
        setState(STATE_ALLOW_CONVERT);
        this.converter = newConverter;
        this.match = newMatch;
        return this;
    }

    /**
     * Add a new {@link Creator} definition.
     * @param id The id referred to by the {@link #withCreator(String, String)}
     * @param className The implementation of {@link Creator} to instansitate.
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator withCreatorType(String id, String className)
    {
        setState(STATE_INIT_CREATE);
        creatorManager.addCreatorType(id, className);
        return this;
    }

    /**
     * Use a {@link Creator} to instansiate a class
     * @param newTypeName A predefined {@link Creator} or one defined by
     * {@link #withCreatorType(String, String)}.
     * @param newScriptName The javascript name of this component
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator withCreator(String newTypeName, String newScriptName)
    {
        setState(STATE_ALLOW_CREATE);
        this.typeName = newTypeName;
        this.scriptName = newScriptName;
        return this;
    }

    /**
     * Add a parameter to whatever is being configured.
     * @param name The name of the parameter
     * @param value The value of the parameter
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator addParam(String name, String value)
    {
        if (params == null)
        {
            params = new HashMap();
        }

        params.put(name, value);
        return this;
    }

    /**
     * Add an include rule to a {@link Creator}.
     * This should be used during a {@link #withCreator(String, String)} call.
     * @param methodName The method name to be allowed
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator include(String methodName)
    {
        accessControl.addIncludeRule(scriptName, methodName);
        return this;
    }

    /**
     * Add an exclude rule to a {@link Creator}
     * This should be used during a {@link #withCreator(String, String)} call.
     * @param methodName The method name to be dis-allowed
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator exclude(String methodName)
    {
        accessControl.addExcludeRule(scriptName, methodName);
        return this;
    }

    /**
     * Add an authorization rule to a {@link Creator}
     * This should be used during a {@link #withCreator(String, String)} call.
     * @param methodName The method name to have a required role
     * @param role The required role for the given method
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator withAuth(String methodName, String role)
    {
        accessControl.addRoleRestriction(scriptName, methodName, role);
        return this;
    }

    /**
     * Add lines to a signature.
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator withSignature()
    {
        setState(STATE_SIGNATURE);
        return this;
    }

    /**
     * Add lines to a signature.
     * @param line The line of text to add to the signature configuration
     * @return <code>this</code> to continue the fluency
     */
    public FluentConfigurator addLine(String line)
    {
        if (null == line)
        {
            return this;
        }

        if (null == signature)
        {
            signature = new StringBuffer();
        }

        signature.append(line + System.getProperty("line.separator"));
        return this;
    }

    /**
     * Because some parts of the configuration require multiple steps, the instance
     * needs to maintain a state across invocations. Whenever the state is changed
     * by calling this method, the instance will 'flush' anything in the queue 
     * applicable to that state EVEN IF the state itself doesn't change. Thus, it's
     * important that the child methods don't call setState() when being invoked. 
     * @param state
     */
    private void setState(int state)
    {
        flush();
        this.state = state;
    }

    /**
     * Takes and configuration that is in progress and calls methods on the
     * various objects to enable that configuration.
     */
    private void flush()
    {
        switch (state)
        {
        case STATE_INIT_CONVERT:
            // do nothing;
            break;

        case STATE_INIT_CREATE:
            // do nothing;
            break;

        case STATE_ALLOW_CONVERT:
            try
            {
                converterManager.addConverter(match, converter, params);
            }
            catch (Exception e)
            {
                log.warn("Failed to add converter of type='" + converter + "', match=" + match + ": ", e);
            }
            params = null;
            match = null;
            converter = null;
            break;

        case STATE_ALLOW_CREATE:
            try
            {
                creatorManager.addCreator(typeName, scriptName, params);
            }
            catch (Exception e)
            {
                log.warn("Failed to add creator of type='" + typeName + "', scriptName=" + scriptName + ": ", e);
            }
            params = null;
            scriptName = null;
            typeName = null;
            break;

        case STATE_SIGNATURE:
            if (signature != null && signature.length() > 0)
            {
                SignatureParser sigp = new SignatureParser(converterManager);
                sigp.parse(signature.toString());
            }
            break;

        default:
            break;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Configurator#configure(org.directwebremoting.Container)
     */
    public void configure(Container container)
    {
        converterManager = (ConverterManager) container.getBean(ConverterManager.class.getName());
        accessControl = (AccessControl) container.getBean(AccessControl.class.getName());
        creatorManager = (CreatorManager) container.getBean(CreatorManager.class.getName());

        configure();

        setState(STATE_COMPLETE);
    }

    /**
     * Used for <allow create .../>
     */
    private String typeName;

    /**
     * Used for <allow create .../>
     */
    private String scriptName;

    /**
     * Used for <allow convert .../>
     */
    private String converter;

    /**
     * Used for <allow convert .../>
     */
    private String match;

    /**
     * holds name / value pairs used in <allow create|convert ... />
     */
    private Map params = null;

    /**
     * holds signature lines
     */
    private StringBuffer signature;

    /**
     * What section of a configuration are we in?
     */
    private int state = -1;

    /**
     * The ConverterManager that we are configuring
     */
    private ConverterManager converterManager;

    /**
     * The AccessControl that we are configuring
     */
    private AccessControl accessControl;

    /**
     * The CreatorManager that we are configuring
     */
    private CreatorManager creatorManager;

    /**
     * {@link #state} to say we are working in {@link #withCreatorType(String, String)}
     */
    private static final int STATE_INIT_CREATE = 0;

    /**
     * {@link #state} to say we are working in {@link #withConverterType(String, String)}
     */
    private static final int STATE_INIT_CONVERT = 1;

    /**
     * {@link #state} to say we are working in {@link #withCreator(String, String)}
     */
    private static final int STATE_ALLOW_CREATE = 2;

    /**
     * {@link #state} to say we are working in {@link #withConverter(String, String)}
     */
    private static final int STATE_ALLOW_CONVERT = 3;

    /**
     * {@link #state} to say we are working in {@link #withSignature()}
     */
    private static final int STATE_SIGNATURE = 4;

    /**
     * {@link #state} to say {@link #configure()} has completed
     */
    private static final int STATE_COMPLETE = 5;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(FluentConfigurator.class);
}
