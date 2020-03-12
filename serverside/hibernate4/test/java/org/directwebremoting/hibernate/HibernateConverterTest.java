package org.directwebremoting.hibernate;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.ServletContext;

import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.convert.PrimitiveConverter;
import org.directwebremoting.convert.mapped.Hibernate4Ex;
import org.directwebremoting.convert.mapped.Hibernate4NestEx;
import org.directwebremoting.convert.mapped.Hibernate4sEx;
import org.directwebremoting.impl.TestEnvironment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.directwebremoting.convert.AllConverterTest.*;

/**
 * The tests for the <code>PrimitiveConverter</code> class.
 * @see PrimitiveConverter
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Ignore // Do not include in default run as it needs custom environment setup
public class HibernateConverterTest
{
    private static SessionFactory sessionFactory;

    @Before
    public void start() throws Exception {
        TestEnvironment.configureFromClassResource("/dwr.xml");

        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory(
           new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry());
        Assert.assertNotNull(sessionFactory);
    }

    @After
    public void stop() {
        sessionFactory.close();
    }

    @Test
    public void hibernateInit() throws Exception
    {
        Session session = sessionFactory.openSession();

        // Verify the database rows.
        Hibernate4Ex parent = (Hibernate4Ex) session.load(Hibernate4Ex.class, 1);
        Assert.assertEquals("fred", parent.getName());
        Set<Hibernate4NestEx> children = parent.getChildren();
        Assert.assertEquals(1, children.size());

        // Verify the foreign key relationship data.
        Hibernate4NestEx child = children.iterator().next();
        Assert.assertEquals("jim", child.getName());
        Assert.assertEquals(parent, child.getOwner());


        session.close();
    }

    @Test
    public void hibernateBasicsConvert() throws Exception
    {
        // Checks that do not need DB access
        assertInboundConversion("null", Hibernate4Ex.class, null);
        assertInboundConversion("{ }", Hibernate4Ex.class, new Hibernate4Ex());
        assertInboundConversion("{ id:int:1,name:string:fred }", Hibernate4Ex.class, new Hibernate4Ex(1, "fred"));
        assertOutboundConversion(new Hibernate4Ex(), "{children:[],id:null,name:null}");
    }

    @Test
    public void hibernateNoProxyOutbound() throws Exception {
    	Hibernate4Ex ex = new Hibernate4Ex();
        Hibernate4NestEx exnest = new Hibernate4NestEx();
        exnest.setId(1);
        exnest.setName("Nest");
        ex.getChildren().add(exnest);
        assertOutboundConversion(ex, "{children:[{id:1,name:\"Nest\",owner:null}],id:null,name:null}");

        Hibernate4NestEx exnest2 = new Hibernate4NestEx();
        exnest2.setId(2);
        exnest2.setName("Nest2");
        ex.getChildren().add(exnest2);
        assertOutboundConversion(ex, "{children:[{id:1,name:\"Nest\",owner:null},{id:2,name:\"Nest2\",owner:null}],id:null,name:null}");

        Hibernate4NestEx exnest3 = new Hibernate4NestEx();
        exnest3.setId(3);
        exnest3.setName("Nest3");
        ex.getChildren().add(exnest3);
        assertOutboundConversion(ex, "{children:[{id:1,name:\"Nest\",owner:null},{id:2,name:\"Nest2\",owner:null},{id:3,name:\"Nest3\",owner:null}],id:null,name:null}");
    }

    @Test
    public void hibernateInbound() throws Exception {
    	Hibernate4Ex ex = new Hibernate4Ex();
    	ex.setId(1);
    	ex.setName("Matt");
        Hibernate4NestEx exnest = new Hibernate4NestEx();
        exnest.setId(1);
        exnest.setName("Nest");
        assertInboundConversion("{children:set:[],id:int:1,name:string:Matt}", Hibernate4Ex.class, ex);
        ex.getChildren().add(exnest);
//        assertInboundConversion("{children:[{id:1,name:\"Nest\",owner:null}],id:1,name:\"Matt\"}", Hibernate4Ex.class, ex);
    }

    @Test
    @Ignore
    public void hibernate4sConvert() throws Exception
    {
        // Hibernate 3 setup, keep the session open and use the bean converter
        Session session = sessionFactory.openSession();

        ServletContext servletContext = WebContextFactory.get().getServletContext();
        H4SessionAjaxFilter.setSessionFactory(servletContext, sessionFactory);

        Transaction transaction = session.beginTransaction();
        final Hibernate4sEx parent = (Hibernate4sEx) session.load(Hibernate4sEx.class, 1);
        parent.getId();
        parent.getName();
        //parent.getChildren();

        transaction.commit();

        H4SessionAjaxFilter filter = new H4SessionAjaxFilter();
        filter.doFilter(null, null, null, new AjaxFilterChain()
        {
            public Object doFilter(Object obj, Method method, Object[] params) throws Exception
            {
                // This is way to fragile, but it will do for now
                assertOutboundConversion(parent, "var s0=[];var s1={};s0[0]=s1;s1.id=2;s1.name=\"jim\";s1.owner=null;{children:s0,id:1,name:\"fred\"}");
                return null;
            }
        });

        session.close();
    }

    @Test
    public void hibernate4Convert() throws Exception
    {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Hibernate4Ex parent = (Hibernate4Ex) session.load(Hibernate4Ex.class, 1);
        parent.getId();
        parent.getName();
        //parent.getChildren();

        transaction.commit();
        session.close();

        // This is way to fragile, but it will do for now
        assertOutboundConversion(parent, "{children:null,id:1,name:\"fred\"}");
    }
}
