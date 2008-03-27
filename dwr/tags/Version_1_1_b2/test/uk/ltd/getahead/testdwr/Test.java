/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ltd.getahead.testdwr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.WebContextFactory;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * Methods to help unit test DWR.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: Test.java,v 1.11 2005/02/12 16:20:42 joe_walker Exp $
 */
public class Test
{
    /**
     * 
     */
    public void throwNPE()
    {
        throw new NullPointerException();
    }

    /**
     * @param wait
     * @return wait
     */
    public int waitFor(int wait)
    {
        synchronized (Thread.currentThread())
        {
            try
            {
                Thread.currentThread().wait(wait);
                return wait;
            }
            catch (InterruptedException ex)
            {
                return 0;
            }
        }
    }

    /**
     * 
     */
    public void doNothing()
    {
    }

    /**
     * @return obja
     */
    public ObjA getLooped()
    {
        ObjA objA = new ObjA();
        ObjB objB = new ObjB();
        objA.setObjB(objB);
        objB.setObjA(objA);
        return objA;
    }

    /**
     * 
     */
    public void voidParam()
    {
    }

    /**
     * @param test
     * @return string
     */
    public boolean booleanParam(boolean test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public byte byteParam(byte test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public char charParam(char test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public short shortParam(short test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public int intParam(int test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public long longParam(long test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public float floatParam(float test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public double doubleParam(double test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public String stringParam(String test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public boolean[] booleanArrayParam(boolean[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public char[] charArrayParam(char[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public byte[] byteArrayParam(byte[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public short[] shortArrayParam(short[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public int[] intArrayParam(int[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public long[] longArrayParam(long[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public float[] floatArrayParam(float[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public double[] doubleArrayParam(double[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public double[][] double2DArrayParam(double[][] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public double[][][] double3DArrayParam(double[][][] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public double[][][][] double4DArrayParam(double[][][][] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public double[][][][][] double5DArrayParam(double[][][][][] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public BigInteger bigIntegerParam(BigInteger test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public BigDecimal bigDecimalParam(BigDecimal test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public String[] stringArrayParam(String[] test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public Collection stringCollectionParam(Collection test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public LinkedList stringLinkedListParam(LinkedList test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public ArrayList stringArrayListParam(ArrayList test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public List stringListParam(List test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public Set stringSetParam(Set test)
    {
        return test;
    }

    /**
     * @param test
     * @return set of beans
     */
    public Set testBeanSetParam(Set test)
    {
        if (test.size() > 1)
        {
            for (Iterator it = test.iterator(); it.hasNext();)
            {
                TestBean ele = (TestBean) it.next();
                TestBean ignore = ele;
                ele = ignore;
            }
        }

        return test;
    }

    /**
     * @param test
     * @return set of beans
     */
    public List testBeanListParam(List test)
    {
        if (test.size() > 1)
        {
            for (Iterator it = test.iterator(); it.hasNext();)
            {
                TestBean ele = (TestBean) it.next();
                TestBean ignore = ele;
                ele = ignore;
            }
        }

        return test;
    }

    /**
     * @param test
     * @return string
     */
    public HashSet stringHashSetParam(HashSet test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public TreeSet stringTreeSetParam(TreeSet test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public TestBean testBeanParam(TestBean test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public Map stringStringMapParam(Map test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public Map charTestBeanMapParam(Map test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public Map stringStringHashMapParam(HashMap test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public Map stringStringTreeMapParam(TreeMap test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    public TestBean[] testBeanArrayParam(TestBean[] test)
    {
        return test;
    }

    /**
     * @param type
     * @return test bean
     */
    public TestBean inheritanceTest(int type)
    {
        switch (type)
        {
        case 0:
            return new TestBean();

        case 1:
            return new StaticInnerSubTestBean();

        case 2:
            return new InnerSubTestBean();

        case 3:
            return new TestBean() { };

        case 4:
            return (TestBean) Proxy.newProxyInstance(TestBean.class.getClassLoader(), new Class[] { TestBean.class }, new TestBeanInvocationHandler());

        default :
            throw new IllegalArgumentException("" + type); //$NON-NLS-1$
        }
    }

    /** */
    public class InnerSubTestBean extends TestBean
    {
    }

    /** */
    public static class StaticInnerSubTestBean extends TestBean
    {
    }

    static class TestBeanInvocationHandler implements InvocationHandler
    {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            if (method.getName().equals("getInteger")) //$NON-NLS-1$
            {
                return new Integer(42);
            }

            if (method.getName().equals("getString")) //$NON-NLS-1$
            {
                return "Slartibartfast"; //$NON-NLS-1$
            }

            if (method.getName().equals("equals")) //$NON-NLS-1$
            {
                return new Boolean(equals(args[0]));
            }

            if (method.getName().equals("hashCode")) //$NON-NLS-1$
            {
                return new Integer(hashCode());
            }

            log.error("Failed on method: " + method); //$NON-NLS-1$
            return null;
        }
    }

    /**
     * @param client 
     * @return Debugging map
     */
    public Map dateTest(Date client)
    {
        Date server = new Date();

        Map reply = new HashMap();

        reply.put("client-object", client); //$NON-NLS-1$
        reply.put("client-string", client.toString()); //$NON-NLS-1$
        reply.put("server-object", server); //$NON-NLS-1$
        reply.put("server-string", server.toString()); //$NON-NLS-1$

        return reply;
    }

    /**
     * @param type
     * @return test bean
     */
    public Foo inheritanceFooTest(int type)
    {
        switch (type)
        {
        case 0:
            return new InnerFoo();

        case 1:
            return new Foo() { public String getString() { return "anon foo"; }}; //$NON-NLS-1$

        case 4:
            return (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(), new Class[] { Foo.class }, new TestBeanInvocationHandler());

        default :
            throw new IllegalArgumentException("" + type); //$NON-NLS-1$
        }
    }

    /** */
    public interface Foo
    {
        /** @return string */
        String getString();
    }

    /** */
    public class InnerFoo implements Foo
    {
        public String getString() { return "inner foo"; } //$NON-NLS-1$
    }

    /**
     * @param req
     * @return string
     */
    public String httpServletRequestParam(HttpServletRequest req)
    {
        return req.getRemoteAddr();
    }

    /**
     * @param req
     * @param i
     * @param resp
     * @param s
     * @param session
     * @param ss
     * @param scx
     * @param d
     * @param scfg
     * @return string
     */
    public String httpObjectParams(HttpServletRequest req, int i, HttpServletResponse resp, String s, HttpSession session, short[] ss, ServletContext scx, Date d, ServletConfig scfg)
    {
        return req.getRemoteAddr() + i + resp.hashCode() + s + session.getId() + ss.length + scx.getMajorVersion() + d.getTime() + scfg.getServletName();
    }

    /**
     * @return nest
     */
    public TestBean[] getNestingTest()
    {
        TestBean a = new TestBean(0, "!\"$%^&*()_1", null); //$NON-NLS-1$
        TestBean b = new TestBean(0, "!\"$%^&*()_2", a); //$NON-NLS-1$
        TestBean c = new TestBean(0, "!\"$%^&*()_3", b); //$NON-NLS-1$
        TestBean d = new TestBean(0, "!\"$%^&*()_4", c); //$NON-NLS-1$

        TestBean[] reply = new TestBean[]
        {
            a, c, d, d,
        };

        return reply;
    }

    /**
     * @param param1
     * @param param2
     * @return string
     */
    public String stringStringParam(String param1, String param2)
    {
        return "param1='" + param1 + "' param2='" + param2 + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * @param param
     * @param delay
     * @return string
     * @throws InterruptedException 
     */
    public String slowStringParam(String param, long delay) throws InterruptedException
    {
        log.debug("About to wait for: " + delay); //$NON-NLS-1$
        synchronized (this)
        {
            wait(delay);
        }
        log.debug("Done waiting for: " + delay); //$NON-NLS-1$

        return param;
    }

    /**
     * @param data The results of the current test
     * @return A summary of all the received results
     */
    public Map reply(Map data)
    {
        String failReport = (String) data.get("failreport"); //$NON-NLS-1$

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String userAgentHttp = request.getHeader("User-Agent"); //$NON-NLS-1$
        userAgentHttp = simplfyUserAgent(userAgentHttp);
        if (userAgentHttp.length() > 100)
        {
            userAgentHttp = userAgentHttp.substring(0, 100);
        }

        if (logfile == null)
        {
            try
            {
                String home = System.getProperty("user.home"); //$NON-NLS-1$
                Writer out = new FileWriter(home + File.separator + "test.log", true); //$NON-NLS-1$
                // URL url = ExecutionContext.get().getServletContext().getResource("/test.log"); //$NON-NLS-1$
                // OutputStream out = url.openConnection().getOutputStream();
                logfile = new PrintWriter(out);
            }
            catch (Exception ex)
            {
                log.error("Failed to open test log file", ex); //$NON-NLS-1$
            }
        }

        if (logfile != null)
        {
            for (Iterator it = data.keySet().iterator(); it.hasNext();)
            {
                String key = (String) it.next();
                if (key.length() > 100)
                {
                    key = key.substring(0, 100);
                }
                String value = (String) data.get(key);
                if (value.length() > 1000)
                {
                    value = value.substring(0, 1000);
                }
                logfile.write("\n" + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$                
            }
            logfile.write("\nuseragent-http=" + userAgentHttp); //$NON-NLS-1$
            logfile.write("\naddr=" + request.getRemoteAddr()); //$NON-NLS-1$
            logfile.write("\n"); //$NON-NLS-1$
            logfile.flush();
        }

        // Update the results summary
        Map failCounts = (Map) results.get(userAgentHttp);
        if (failCounts == null)
        {
            failCounts = new HashMap();
            results.put(userAgentHttp, failCounts);
        }

        Integer reports = (Integer) failCounts.get(failReport);
        if (reports == null)
        {
            reports = new Integer(1);
        }
        else
        {
            reports = new Integer(reports.intValue() + 1);
        }
        failCounts.put(failReport, reports);

        return results;
    }

    /**
     * Attempt to simplfy a user-agent string
     * @param sent The user-agent from the browser
     * @return A user friendly version if possible
     */
    public static String simplfyUserAgent(String sent)
    {
        // Firefox
        int offset = sent.indexOf("Firefox"); //$NON-NLS-1$
        if (offset > 10)
        {
            return sent.substring(offset);
        }

        // IE
        offset = sent.indexOf("MSIE"); //$NON-NLS-1$
        if (offset > 10)
        {
            int end = sent.indexOf(";", offset); //$NON-NLS-1$
            if (end == -1)
            {
                end = sent.length();
            }
            return sent.substring(offset, end);
        }

        // Konq
        offset = sent.indexOf("Konqueror"); //$NON-NLS-1$
        if (offset > 10)
        {
            int end = sent.indexOf(";", offset); //$NON-NLS-1$
            if (end == -1)
            {
                end = sent.length();
            }
            return sent.substring(offset, end);
        }

        // Safari
        offset = sent.indexOf("Safari"); //$NON-NLS-1$
        if (offset > 10)
        {
            int end = sent.indexOf(";", offset); //$NON-NLS-1$
            if (end == -1)
            {
                end = sent.length();
            }
            return sent.substring(offset, end);
        }

        // Opera
        offset = sent.indexOf("Opera"); //$NON-NLS-1$
        if (offset > -1)
        {
            int end = sent.indexOf(" ", offset); //$NON-NLS-1$
            if (end == -1)
            {
                end = sent.length();
            }
            return sent.substring(offset, end);
        }

        return sent;
    }

    /**
     * @return string
     */
    public String delete()
    {
        return "You can't touch me"; //$NON-NLS-1$
    }

    /**
     * @return string
     */
    protected String protectedMethod()
    {
        privateMethod();
        return "You can't touch me"; //$NON-NLS-1$
    }

    /**
     * @return string
     */
    private String privateMethod()
    {
        return "You can't touch me"; //$NON-NLS-1$
    }

    /**
     * @return string
     */
    public static String staticMethod()
    {
        return "static Test.staticMethod() says hello."; //$NON-NLS-1$
    }

    /**
     * @param param1
     * @return string
     */
    public String dangerOverload(String param1)
    {
        return "Test.dangerOverload(" + param1 + ") says hello.";  //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * @return string
     */
    public String dangerOverload()
    {
        return "Test.dangerOverload() says hello."; //$NON-NLS-1$
    }

    /**
     * This method should not be callable
     * @param cx Illegal param
     * @return Error message
     */
    public String error(InboundContext cx)
    {
        return "You should not see this: " + cx; //$NON-NLS-1$
    }

    /**
     * Get some text that has been fetched from a JSP page.
     * @return A part of a web page
     * @throws IOException Forwarded from the insert processing
     * @throws ServletException Forwarded from the insert processing
     */
    public String getInsert() throws ServletException, IOException
    {
        return WebContextFactory.get().forwardToString("/insert.jsp"); //$NON-NLS-1$

        // return "<h1>DWR is working properly</h1><p>To see how DWR is configured in this web application see:</p>" + //$NON-NLS-1$
        //        "<ul><li>The generated <a href='dwr/'>configuration/test pages</a>.</li>" + //$NON-NLS-1$
        //        "<li><a href='test.html'>The unit test suite</a>.</li></ul>"; //$NON-NLS-1$
    }

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(Test.class);

    private static PrintWriter logfile = null;

    private static Map results = new HashMap();
}
