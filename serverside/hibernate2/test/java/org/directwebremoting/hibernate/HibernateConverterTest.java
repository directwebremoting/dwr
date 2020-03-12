package org.directwebremoting.hibernate;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.ServletContext;

import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.convert.PrimitiveConverter;
import org.directwebremoting.convert.mapped.Hibernate2Ex;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

//import static org.directwebremoting.convert.AllConverterTest.*;

/**
 * The tests for the <code>PrimitiveConverter</code> class.
 * @see PrimitiveConverter
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Ignore // Do not include in default run as it needs custom environment setup
public class HibernateConverterTest
{
    public HibernateConverterTest() throws Exception
    {
    }

	@Ignore
    @Test
    public void hibernateInit() throws Exception
    {
        Database.init();

        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Filter-time code (H3SessionAjaxFilter)
        transaction.commit();

        // Shutdown code, when do we need to do this?
        sessionFactory.close();
    }
/*
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
}
