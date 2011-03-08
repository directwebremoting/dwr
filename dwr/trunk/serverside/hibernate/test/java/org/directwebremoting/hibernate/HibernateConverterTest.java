package org.directwebremoting.hibernate;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.ServletContext;

import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.convert.PrimitiveConverter;
import org.directwebremoting.convert.mapped.Hibernate3Ex;
import org.directwebremoting.convert.mapped.Hibernate3NestEx;
import org.directwebremoting.convert.mapped.Hibernate3sEx;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

//import static org.directwebremoting.convert.AllConverterTest.*;

/**
 * The tests for the <code>PrimitiveConverter</code> class.
 * @see PrimitiveConverter
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HibernateConverterTest
{
    public HibernateConverterTest() throws Exception
    {
    }

    @Test
    public void hibernateInit() throws Exception
    {
        Database.init();

        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = config.buildSessionFactory();
		Assert.assertNotNull(sessionFactory);
		
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
/*
    @Test
    @Ignore
    public void hibernateBasicsConvert() throws Exception
    {
        // Checks that do not need DB access
        assertInboundConversion("null", Hibernate3Ex.class, null);
        assertInboundConversion("{ }", Hibernate3Ex.class, new Hibernate3Ex());
        assertInboundConversion("{ id:int:1,name:string:fred }", Hibernate3Ex.class, new Hibernate3Ex(1, "fred"));

        assertOutboundConversion(new Hibernate3Ex(), "var s0=[];{children:s0,id:null,name:null}");
    }

    @Test
    @Ignore
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
    @Ignore
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

    /*
    @Test
    public void convert() throws Exception
    {
    }
    */
}
