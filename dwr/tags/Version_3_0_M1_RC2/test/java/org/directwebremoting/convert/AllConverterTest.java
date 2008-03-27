package org.directwebremoting.convert;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.convert.mapped.BeanEx;
import org.directwebremoting.convert.mapped.Hibernate2Ex;
import org.directwebremoting.convert.mapped.Hibernate3Ex;
import org.directwebremoting.convert.mapped.Hibernate3NestEx;
import org.directwebremoting.convert.mapped.Hibernate3sEx;
import org.directwebremoting.convert.mapped.ObjectEx;
import org.directwebremoting.convert.mapped.ObjectForceEx;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.hibernate.Database;
import org.directwebremoting.hibernate.H3SessionAjaxFilter;
import org.directwebremoting.util.SingletonContainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The tests for the <code>PrimitiveConverter</code> class.
 * @see PrimitiveConverter
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AllConverterTest
{
    public AllConverterTest() throws Exception
    {
    }

    @BeforeClass
    public static void setUp() throws Exception
    {
        singletonContainer.engageThread();
    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        singletonContainer.disengageThread();
    }

    @Test
    public void nullConvert() throws Exception
    {
        assertOutboundConversion(null, "null");
    }

    @Test
    public void booleanConvert() throws Exception
    {
        assertInboundConversion("true", Boolean.class, Boolean.TRUE);
        assertInboundConversion("tRuE", Boolean.class, Boolean.TRUE);
        assertInboundConversion("false", Boolean.class, Boolean.FALSE);
        assertInboundConversion("FALSE", Boolean.class, Boolean.FALSE);
        assertInboundConversion("yes", Boolean.class, Boolean.FALSE);
        assertInboundConversion("null", Boolean.class, Boolean.FALSE);
        assertInboundConversion("", Boolean.class, null);
        assertInboundConversion("true", Boolean.TYPE, Boolean.TRUE);
        assertInboundConversion("tRuE", Boolean.TYPE, Boolean.TRUE);
        assertInboundConversion("false", Boolean.TYPE, Boolean.FALSE);
        assertInboundConversion("FALSE", Boolean.TYPE, Boolean.FALSE);
        assertInboundConversion("yes", Boolean.TYPE, Boolean.FALSE);
        assertInboundConversion("null", Boolean.TYPE, Boolean.FALSE);
        assertInboundConversion("", Boolean.TYPE, Boolean.FALSE);

        assertOutboundConversion(Boolean.TRUE, "true");
        assertOutboundConversion(Boolean.FALSE, "false");
    }

    @Test
    public void byteConvert() throws Exception
    {
        assertInboundConversion("127", Byte.class, new Byte("127"));
        assertInboundConversion("-128", Byte.class, new Byte("-128"));
        assertInboundConversion("0", Byte.class, new Byte("0"));
        assertInboundConversion("", Byte.class, null);
        assertInboundConversion("127", Byte.TYPE, new Byte("127"));
        assertInboundConversion("-128", Byte.TYPE, new Byte("-128"));
        assertInboundConversion("0", Byte.TYPE, new Byte("0"));
        assertInboundConversion("", Byte.TYPE, new Byte("0"));

        assertOutboundConversion(new Byte("127"), "127");
        assertOutboundConversion(new Byte("-128"), "-128");
        assertOutboundConversion(new Byte("0"), "0");

        assertInboundConversionFailure("128", Byte.class);
        assertInboundConversionFailure("-129", Byte.class);
        assertInboundConversionFailure("null", Byte.class);
        assertInboundConversionFailure("128", Byte.TYPE);
        assertInboundConversionFailure("-129", Byte.TYPE);
        assertInboundConversionFailure("null", Byte.TYPE);
    }

    @Test
    public void shortConvert() throws Exception
    {
        assertInboundConversion("-128", Short.class, new Short("-128"));
        assertInboundConversion("0", Short.class, new Short("0"));
        assertInboundConversion("", Short.class, null);
        assertInboundConversion("-128", Short.TYPE, new Short("-128"));
        assertInboundConversion("0", Short.TYPE, new Short("0"));
        assertInboundConversion("", Short.TYPE, new Short("0"));

        assertOutboundConversion(new Short("-128"), "-128");
        assertOutboundConversion(new Short("0"), "0");

        assertInboundConversionFailure("null", Short.class);
        assertInboundConversionFailure("null", Short.TYPE);
    }

    @Test
    public void testIntegerConvert() throws Exception
    {
        assertInboundConversion("-128", Integer.class, new Integer("-128"));
        assertInboundConversion("0", Integer.class, new Integer("0"));
        assertInboundConversion("", Integer.class, null);
        assertInboundConversion("-128", Integer.TYPE, new Integer("-128"));
        assertInboundConversion("0", Integer.TYPE, new Integer("0"));
        assertInboundConversion("", Integer.TYPE, new Integer("0"));

        assertOutboundConversion(new Integer("-128"), "-128");
        assertOutboundConversion(new Integer("0"), "0");

        assertInboundConversionFailure("null", Integer.class);
        assertInboundConversionFailure("null", Integer.TYPE);
    }

    @Test
    public void longConvert() throws Exception
    {
        assertInboundConversion("-128", Long.class, new Long("-128"));
        assertInboundConversion("0", Long.class, new Long("0"));
        assertInboundConversion("", Long.class, null);
        assertInboundConversion("-128", Long.TYPE, new Long("-128"));
        assertInboundConversion("0", Long.TYPE, new Long("0"));
        assertInboundConversion("", Long.TYPE, new Long("0"));

        assertOutboundConversion(new Long("-128"), "-128");
        assertOutboundConversion(new Long("0"), "0");

        assertInboundConversionFailure("null", Long.class);
        assertInboundConversionFailure("null", Long.TYPE);
    }

    @Test
    public void floatConvert() throws Exception
    {
        assertInboundConversion("-12.8", Float.class, new Float("-12.8"));
        assertInboundConversion("0", Float.class, new Float("0"));
        assertInboundConversion("", Float.class, null);
        assertInboundConversion("-12.8", Float.TYPE, new Float("-12.8"));
        assertInboundConversion("0", Float.TYPE, new Float("0"));
        assertInboundConversion("", Float.TYPE, new Float("0"));

        assertOutboundConversion(new Float("-12.8"), "-12.8");
        assertOutboundConversion(new Float("0"), "0.0");

        assertInboundConversionFailure("null", Float.class);
        assertInboundConversionFailure("null", Float.TYPE);
    }

    @Test
    public void doubleConvert() throws Exception
    {
        assertInboundConversion("-12.8", Double.class, new Double("-12.8"));
        assertInboundConversion("0", Double.class, new Double("0"));
        assertInboundConversion("", Double.class, null);
        assertInboundConversion("-12.8", Double.TYPE, new Double("-12.8"));
        assertInboundConversion("0", Double.TYPE, new Double("0"));
        assertInboundConversion("", Double.TYPE, new Double("0"));

        assertOutboundConversion(new Double("-12.8"), "-12.8");
        assertOutboundConversion(new Double("0"), "0.0");

        assertInboundConversionFailure("null", Double.class);
        assertInboundConversionFailure("null", Double.TYPE);
    }

    @Test
    public void characterConvert() throws Exception
    {
        assertInboundConversion("-", Character.class, new Character('-'));
        assertInboundConversion("0", Character.class, new Character('0'));
        assertInboundConversion("\"", Character.class, new Character('\"'));
        assertInboundConversion("\'", Character.class, new Character('\''));
        assertInboundConversion("\u0394", Character.class, new Character('\u0394'));
        assertInboundConversion("-", Character.TYPE, new Character('-'));
        assertInboundConversion("0", Character.TYPE, new Character('0'));
        assertInboundConversion("\"", Character.TYPE, new Character('\"'));
        assertInboundConversion("\'", Character.TYPE, new Character('\''));
        assertInboundConversion("\u0394", Character.TYPE, new Character('\u0394'));

        assertOutboundConversion(new Character('-'), "\"-\"");
        assertOutboundConversion(new Character('0'), "\"0\"");
        assertOutboundConversion(new Character('\"'), "\"\\\"\"");
        assertOutboundConversion(new Character('\''), "\"\\'\"");
        assertOutboundConversion(new Character('\u0394'), "\"\\u0394\"");
        assertOutboundConversion(new Character('-'), "\"-\"");
        assertOutboundConversion(new Character('0'), "\"0\"");
        assertOutboundConversion(new Character('\"'), "\"\\\"\"");
        assertOutboundConversion(new Character('\''), "\"\\'\"");
        assertOutboundConversion(new Character('\u0394'), "\"\\u0394\"");

        assertInboundConversionFailure("", Character.class);
        assertInboundConversionFailure("", Character.TYPE);
        assertInboundConversionFailure("null", Character.class);
        assertInboundConversionFailure("null", Character.TYPE);
    }

    @Test
    public void stringConvert() throws Exception
    {
        assertInboundConversion("-", String.class, "-");
        assertInboundConversion("0", String.class, "0");
        assertInboundConversion("\"", String.class, "\"");
        assertInboundConversion("\'", String.class, "\'");
        assertInboundConversion("\u0394", String.class, "\u0394");
        assertInboundConversion("", String.class, "");
        assertInboundConversion("null", String.class, "null");

        assertOutboundConversion("-", "\"-\"");
        assertOutboundConversion("0", "\"0\"");
        assertOutboundConversion("\"", "\"\\\"\"");
        assertOutboundConversion("\'", "\"\\'\"");
        assertOutboundConversion("\u0394", "\"\\u0394\"");
        assertOutboundConversion("", "\"\"");
        assertOutboundConversion("null", "\"null\"");
    }

    @Test
    public void bigIntegerConvert() throws Exception
    {
        assertInboundConversion("-12", BigInteger.class, new BigInteger("-12"));
        assertInboundConversion("0", BigInteger.class, new BigInteger("0"));
        assertInboundConversion("", BigInteger.class, null);

        assertOutboundConversion(new BigInteger("-12"), "-12");
        assertOutboundConversion(new BigInteger("0"), "0");

        assertInboundConversionFailure("null", BigInteger.class);
    }

    @Test
    public void bigDecimalConvert() throws Exception
    {
        assertInboundConversion("-12", BigDecimal.class, new BigDecimal("-12"));
        assertInboundConversion("0", BigDecimal.class, new BigDecimal("0"));
        assertInboundConversion("", BigDecimal.class, null);

        assertOutboundConversion(new BigDecimal("-12"), "-12");
        assertOutboundConversion(new BigDecimal("0"), "0");

        assertInboundConversionFailure("null", BigDecimal.class);
    }

    @Test
    public void dateConvert() throws Exception
    {
        assertInboundConversion("1104537600000", Date.class, testDate);
        assertInboundConversion("null", Date.class, null);
        assertInboundConversion("1104537600000", java.sql.Date.class, testDate);
        assertInboundConversion("null", java.sql.Date.class, null);

        assertOutboundConversion(testDate, "new Date(1104537600000)");
    }

    @Test
    public void beanConvert() throws Exception
    {
        assertInboundConversion("null", BeanEx.class, null);
        assertInboundConversion("{ }", BeanEx.class, new BeanEx());
        assertInboundConversion("{ name:string:fred }", BeanEx.class, new BeanEx("fred"));
        assertInboundConversion("{ wrong:string:fred }", BeanEx.class, new BeanEx());

        assertOutboundConversion(new BeanEx(), "{name:null}");
        assertOutboundConversion(new BeanEx("fred"), "{name:\"fred\"}");
        assertOutboundConversion(new BeanEx(), "{name:null}");
    }

    @Test
    public void objectConvert() throws Exception
    {
        assertInboundConversion("null", ObjectEx.class, null);
        assertInboundConversion("{ }", ObjectEx.class, new ObjectEx());
        assertInboundConversion("{ name:string:fred }", ObjectEx.class, new ObjectEx("fred"));
        assertInboundConversion("{ wrong:string:fred }", ObjectEx.class, new ObjectEx());
        assertInboundConversion("{ hidden:string:fred }", ObjectEx.class, new ObjectEx());
        assertInboundConversion("null", ObjectForceEx.class, null);
        assertInboundConversion("{ }", ObjectForceEx.class, new ObjectForceEx());
        assertInboundConversion("{ name:string:fred }", ObjectForceEx.class, new ObjectForceEx("fred"));
        assertInboundConversion("{ wrong:string:fred }", ObjectForceEx.class, new ObjectForceEx());

        assertOutboundConversion(new ObjectEx(), "{name:null}");
        assertOutboundConversion(new ObjectEx("fred"), "{name:\"fred\"}");
        assertOutboundConversion(new ObjectEx(), "{name:null}");
        assertOutboundConversion(new ObjectEx(), "{name:null}");
        assertOutboundConversion(new ObjectForceEx(), "{name:null}");
        assertOutboundConversion(new ObjectForceEx("fred"), "{name:\"fred\"}");
        assertOutboundConversion(new ObjectForceEx(), "{name:null}");
    }

    @Test
    public void hibernateInit() throws Exception
    {
        Database.init();

        Configuration config = new Configuration();
        config.configure("hibernate3.cfg.xml");
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // Filter-time code (H3SessionAjaxFilter)

        // Run-time code. Probably won't use HibernateUtil2 
        Hibernate3Ex parent = (Hibernate3Ex) session.load(Hibernate3Ex.class, 1);

        // Some random checks
        Assert.assertEquals("fred", parent.getName());
        Set<Hibernate3NestEx> children = parent.getChildren();
        Assert.assertEquals(1, children.size());
        Hibernate3NestEx child = children.iterator().next();
        Assert.assertEquals("jim", child.getName());
        Assert.assertEquals(parent, child.getOwner());

        // Filter-time code (H3SessionAjaxFilter)
        transaction.commit();

        // Shutdown code, when do we need to do this?
        sessionFactory.close();
    }

    @Test
    public void hibernateBasicsConvert() throws Exception
    {
        // Checks that do not need DB access
        assertInboundConversion("null", Hibernate3Ex.class, null);
        assertInboundConversion("{ }", Hibernate3Ex.class, new Hibernate3Ex());
        assertInboundConversion("{ id:int:1,name:string:fred }", Hibernate3Ex.class, new Hibernate3Ex(1, "fred"));

        assertOutboundConversion(new Hibernate3Ex(), "var s0=[];{children:s0,id:null,name:null}");
    }

    @Test
    public void hibernate3sConvert() throws Exception
    {
        Database.init();

        // Hibernate 3 setup, keep the session open and use the bean converter
        Configuration config = new Configuration();
        config.configure("hibernate3.cfg.xml");
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        ServletContext servletContext = WebContextFactory.get().getServletContext();
        H3SessionAjaxFilter.setSessionFactory(servletContext, sessionFactory);

        Transaction transaction = session.beginTransaction();
        final Hibernate3sEx parent = (Hibernate3sEx) session.load(Hibernate3sEx.class, 1);
        parent.getId();
        parent.getName();
        //parent.getChildren();

        transaction.commit();

        H3SessionAjaxFilter filter = new H3SessionAjaxFilter();
        filter.doFilter(null, null, null, new AjaxFilterChain()
        {
            public Object doFilter(Object obj, Method method, Object[] params) throws Exception
            {
                // This is way to fragile, but it will do for now
                assertOutboundConversion(parent, "var s0=[];var s1={};s0[0]=s1;s1.id=2;s1.name=\"jim\";s1.owner=null;{children:s0,id:1,name:\"fred\"}");
                return null;
            }
        });

        sessionFactory.close();
    }

    @Test
    public void hibernate3Convert() throws Exception
    {
        Database.init();

        // Hibernate 3 setup, close the session and use the h3 converter
        Configuration config = new Configuration();
        config.configure("hibernate3.cfg.xml");
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Hibernate3Ex parent = (Hibernate3Ex) session.load(Hibernate3Ex.class, 1);
        parent.getId();
        parent.getName();
        //parent.getChildren();

        transaction.commit();
        sessionFactory.close();

        // This is way to fragile, but it will do for now
        assertOutboundConversion(parent, "{children:null,id:1,name:\"fred\"}");
    }

    @Ignore("Hibernate 2 appears broken")
    @Test
    public void hibernate2Convert() throws Exception
    {
        Database.init();

        // Hibernate 2 setup, close the session and use the h2 converter
        net.sf.hibernate.cfg.Configuration config = new net.sf.hibernate.cfg.Configuration();
        config.configure(getClass().getResource("/hibernate2.cfg.xml"));
        net.sf.hibernate.SessionFactory sessionFactory = config.buildSessionFactory();
        net.sf.hibernate.Session session = sessionFactory.openSession();
        net.sf.hibernate.Transaction transaction = session.beginTransaction();

        Hibernate2Ex parent = (Hibernate2Ex) session.load(Hibernate2Ex.class, 1);
        // This is way to fragile, but it will do for now
        assertOutboundConversion(parent, "var s0={};var s1=[];var s2={};s0.children=s1;s0.id=1;s0.name=\"fred\";s1[0]=s2;s2.id=2;s2.name=\"jim\";s2.owner=s0;s0");

        transaction.commit();
        sessionFactory.close();
    }

    /*
    @Test
    public void convert() throws Exception
    {
    }
    */

    protected void assertInboundConversion(String input, Class<?> convertTo, Object expected)
    {
        ConverterManager converterManager = singletonContainer.getConverterManager();
        InboundContext ctx = new InboundContext();

        String explanation = "Convert \"" + input + "\" to " + convertTo.getName();

        boolean isException = expected instanceof Class && (Exception.class.isAssignableFrom((Class<?>) expected));

        if (isException)
        {
            try
            {
                new InboundVariable(ctx, null, "type", input);
                Assert.fail();
            }
            catch (Exception ex)
            {
                Assert.assertEquals(explanation, ex.getClass(), expected);
            }
        }
        else
        {
            try
            {
                InboundVariable iv = new InboundVariable(ctx, null, "type", input);
                Object result = converterManager.convertInbound(convertTo, iv, ctx, null);
                Assert.assertEquals(explanation, result, expected);
            }
            catch (Exception ex)
            {
                log.error(explanation, ex);
                Assert.fail(explanation);
            }
        }
    }

    protected void assertInboundConversionFailure(String input, Class<?> convertTo)
    {
        ConverterManager converterManager = singletonContainer.getConverterManager();
        InboundContext ctx = new InboundContext();

        String explanation = "Convert \"" + input + "\" to " + convertTo.getSimpleName();

        try
        {
            InboundVariable iv = new InboundVariable(ctx, null, "type", input);
            converterManager.convertInbound(convertTo, iv, ctx, null);
            Assert.fail();
        }
        catch (Exception ex)
        {
            Assert.assertEquals(explanation, ex.getClass(), MarshallException.class);
        }
    }

    protected void assertOutboundConversion(Object input, String expected) throws MarshallException
    {
        ConverterManager converterManager = singletonContainer.getConverterManager();
        OutboundContext ctx = new OutboundContext(false);

        OutboundVariable result = converterManager.convertOutbound(input, ctx);

        Assert.assertNotNull(result);

        String script = result.getDeclareCode() + result.getBuildCode() + result.getAssignCode();
        script = script.replace("\r", "");
        script = script.replace("\n", "");

        Assert.assertEquals(expected, script);
    }

    private static final Log log = LogFactory.getLog(AllConverterTest.class);

    private static SingletonContainer singletonContainer;
    private static final DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private static Date testDate;
    static
    {
        try
        {
            testDate = format.parse("01-01-2005");
            singletonContainer = new SingletonContainer();
        }
        catch (Exception ex)
        {
            log.error("init failure", ex);
        }
    }
}
