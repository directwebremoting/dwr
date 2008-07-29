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
package com.example.dwr.marshall;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.proxy.ScriptProxy;
import org.xml.sax.SAXParseException;

/**
 * Methods to help unit test DWR.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@SuppressWarnings({"UnnecessaryFullyQualifiedName"})
public class Test
{
    public void throwNPE()
    {
        // This is exported by dwr.xml
        throw new NullPointerException("NullPointerException");
    }

    public void throwIAE()
    {
        // This is NOT exported by dwr.xml
        throw new IllegalArgumentException("IllegalArgumentException");
    }

    public void throwSPE() throws SAXParseException
    {
        // This is exported by dwr.xml as a result of it being a SAXException
        throw new SAXParseException("SAXParseException", "publicId", "systemId", 42, 24, new NullPointerException("NullPointerException"));
    }

    public int waitFor(int wait)
    {
        try
        {
            Thread.sleep(wait);
            return wait;
        }
        catch (InterruptedException ex)
        {
            return 0;
        }
    }

    public void slowAsync(final int wait, final String function)
    {
        WebContext context = WebContextFactory.get();
        ScriptSession session = context.getScriptSession();
        final ScriptProxy proxy = new ScriptProxy(session);

        new Thread() {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(wait);
                    proxy.addFunctionCall(function);
                }
                catch (InterruptedException ex)
                {
                }
            }
        }.start();
    }

    public void doNothing()
    {
    }

    public boolean areIdentical(List<?> a, List<?> b)
    {
        return a == b;
    }

    public ObjA getLooped()
    {
        ObjA objA = new ObjA();
        ObjB objB = new ObjB();
        objA.setObjB(objB);
        objB.setObjA(objA);
        return objA;
    }

    public ObjA testLooped(ObjA objA)
    {
        ObjA nestedA = objA.getObjB().getObjA();

        if (nestedA != objA)
        {
            throw new IllegalStateException("Non matching obja != obja.objb.obja");
        }

        if (nestedA.getObjB() != objA.getObjB())
        {
            throw new IllegalStateException("Non matching objb != objb.obja.objb");
        }

        return objA;
    }

    public void voidParam()
    {
    }

    public boolean booleanParam(boolean test)
    {
        return test;
    }

    public byte byteParam(byte test)
    {
        return test;
    }

    public char charParam(char test)
    {
        return test;
    }

    public short shortParam(short test)
    {
        return test;
    }

    public int intParam(int test)
    {
        return test;
    }

    public long longParam(long test)
    {
        return test;
    }

    public float floatParam(float test)
    {
        return test;
    }

    public double doubleParam(double test)
    {
        return test;
    }

    public String stringParam(String test)
    {
        return test;
    }

    public boolean[] booleanArrayParam(boolean[] test)
    {
        return test;
    }

    public char[] charArrayParam(char[] test)
    {
        return test;
    }

    public byte[] byteArrayParam(byte[] test)
    {
        return test;
    }

    public short[] shortArrayParam(short[] test)
    {
        return test;
    }

    public int[] intArrayParam(int[] test)
    {
        return test;
    }

    public long[] longArrayParam(long[] test)
    {
        return test;
    }

    public float[] floatArrayParam(float[] test)
    {
        return test;
    }

    public double[] doubleArrayParam(double[] test)
    {
        return test;
    }

    public double[][] double2DArrayParam(double[][] test)
    {
        return test;
    }

    public double[][][] double3DArrayParam(double[][][] test)
    {
        return test;
    }

    public double[][][][] double4DArrayParam(double[][][][] test)
    {
        return test;
    }

    public double[][][][][] double5DArrayParam(double[][][][][] test)
    {
        return test;
    }

    public BigInteger bigIntegerParam(BigInteger test)
    {
        return test;
    }

    public BigDecimal bigDecimalParam(BigDecimal test)
    {
        return test;
    }

    public String[] stringArrayParam(String[] test)
    {
        return test;
    }

    public Collection<String> stringCollectionParam(Collection<String> test)
    {
        return test;
    }

    public LinkedList<String> stringLinkedListParam(LinkedList<String> test)
    {
        return test;
    }

    public ArrayList<String> stringArrayListParam(ArrayList<String> test)
    {
        return test;
    }

    public List<String> stringListParam(List<String> test)
    {
        return test;
    }

    public Set<String> stringSetParam(Set<String> test)
    {
        return test;
    }

    public org.dom4j.Element dom4jElementParam(org.dom4j.Element test)
    {
        return test;
    }

    public org.dom4j.Document dom4jDocumentParam(org.dom4j.Document test)
    {
        return test;
    }

    public nu.xom.Element xomElementParam(nu.xom.Element test)
    {
        return test;
    }

    public nu.xom.Document xomDocumentParam(nu.xom.Document test)
    {
        return test;
    }

    public org.jdom.Element jdomElementParam(org.jdom.Element test)
    {
        return test;
    }

    public org.jdom.Document jdomDocumentParam(org.jdom.Document test)
    {
        return test;
    }

    public org.w3c.dom.Element domElementParam(org.w3c.dom.Element test)
    {
        return test;
    }

    public org.w3c.dom.Document domDocumentParam(org.w3c.dom.Document test)
    {
        return test;
    }

    public Set<TestBean> testBeanSetParam(Set<TestBean> test)
    {
        if (test.size() > 1)
        {
            for (Iterator<TestBean> it = test.iterator(); it.hasNext();)
            {
                TestBean ele = it.next();
                TestBean ignore = ele;
                ele = ignore;
            }
        }

        return test;
    }

    public List<TestBean> testBeanListParam(List<TestBean> test)
    {
        if (test.size() > 1)
        {
            for (Iterator<TestBean> it = test.iterator(); it.hasNext();)
            {
                TestBean ele = it.next();
                TestBean ignore = ele;
                ele = ignore;
            }
        }

        return test;
    }

    public HashSet<String> stringHashSetParam(HashSet<String> test)
    {
        return test;
    }

    public TreeSet<String> stringTreeSetParam(TreeSet<String> test)
    {
        return test;
    }

    public TestBean testBeanParam(TestBean test)
    {
        return test;
    }

    public Map<String, String> stringStringMapParam(Map<String, String> test)
    {
        return test;
    }

    public Map<Character, TestBean> charTestBeanMapParam(Map<Character, TestBean> test)
    {
        return test;
    }

    public Map<String, String> stringStringHashMapParam(HashMap<String, String> test)
    {
        return test;
    }

    public Map<String, String> stringStringTreeMapParam(TreeMap<String, String> test)
    {
        return test;
    }

    public TestBean[] testBeanArrayParam(TestBean[] test)
    {
        return test;
    }

    public List<Set<Map<String, TestBean>>> testComplex(List<Set<Map<String, TestBean>>> test)
    {
        return test;
    }

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
            throw new IllegalArgumentException("" + type);
        }
    }

    public class InnerSubTestBean extends TestBean
    {
    }

    public static class StaticInnerSubTestBean extends TestBean
    {
    }

    static class TestBeanInvocationHandler implements InvocationHandler
    {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            if ("getInteger".equals(method.getName()))
            {
                return 42;
            }

            if ("getString".equals(method.getName()))
            {
                return "Slartibartfast";
            }

            if ("equals".equals(method.getName()))
            {
                return equals(args[0]);
            }

            if ("hashCode".equals(method.getName()))
            {
                return hashCode();
            }

            log.error("Failed on method: " + method);
            return null;
        }
    }

    public Map<String, Comparable<?>> dateTest(Date client)
    {
        Date server = new Date();

        Map<String, Comparable<?>> reply = new HashMap<String, Comparable<?>>();

        reply.put("client-object", client);
        reply.put("client-string", client.toString());
        reply.put("server-object", server);
        reply.put("server-string", server.toString());

        return reply;
    }

    public Foo inheritanceFooTest(int type)
    {
        switch (type)
        {
        case 0:
            return new InnerFoo();

        case 1:
            return new Foo() { public String getString() { return "anon foo"; }};

        case 4:
            return (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(), new Class[] { Foo.class }, new TestBeanInvocationHandler());

        default :
            throw new IllegalArgumentException("" + type);
        }
    }

    public interface Foo
    {
        String getString();
    }

    public class InnerFoo implements Foo
    {
        public String getString() { return "inner foo"; }
    }

    public String httpServletRequestParam(HttpServletRequest req)
    {
        return req.getRemoteAddr();
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> listParameters(HttpServletRequest request)
    {
        Map<String, String> reply = new HashMap<String, String>();

        Enumeration<String> names = request.getAttributeNames();
        while (names.hasMoreElements())
        {
            String name = names.nextElement();
            String value = request.getAttribute(name).toString();
            reply.put(name, value);
        }

        return reply;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> listHeaders(HttpServletRequest request)
    {
        Map<String, String> reply = new HashMap<String, String>();

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements())
        {
            String name = names.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            StringBuilder value = new StringBuilder();
            while (values.hasMoreElements())
            {
                String single = values.nextElement();
                value.append(single);
                if (values.hasMoreElements())
                {
                    value.append(", ");
                }
            }

            reply.put(name, value.toString());
        }

        return reply;
    }

    public String httpObjectParams(HttpServletRequest req, int i, HttpServletResponse resp, String s, HttpSession session, short[] ss, ServletContext scx, Date d, ServletConfig scfg)
    {
        return req.getRemoteAddr() + i + resp.hashCode() + s + session.getId() + ss.length + scx.getMajorVersion() + d.getTime() + scfg.getServletName();
    }

    public TestBean[] getNestingTest()
    {
        TestBean a = new TestBean(0, "!\"$%^&*()_1", null);
        TestBean b = new TestBean(0, "!\"$%^&*()_2", a);
        TestBean c = new TestBean(0, "!\"$%^&*()_3", b);
        TestBean d = new TestBean(0, "!\"$%^&*()_4", c);

        TestBean[] reply = new TestBean[]
        {
            a, c, d, d,
        };

        return reply;
    }

    public String slowStringParam(String param, long delay) throws InterruptedException
    {
        log.debug("About to wait for: " + delay);
        synchronized (this)
        {
            wait(delay);
        }
        log.debug("Done waiting for: " + delay);

        return param;
    }

    public String delete()
    {
        return "You can't touch me";
    }

    protected String protectedMethod()
    {
        privateMethod();
        return "You can't touch me";
    }

    private String privateMethod()
    {
        return "You can't touch me";
    }

    public static String staticMethod()
    {
        return "static Test.staticMethod() says hello.";
    }

    public String dangerOverload(String param1)
    {
        return "Test.dangerOverload(" + param1 + ") says hello.";
    }

    public String dangerOverload()
    {
        return "Test.dangerOverload() says hello.";
    }

    public String error(InboundContext cx)
    {
        return "You should not see this: " + cx;
    }

    public String serverChecks()
    {
        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        scriptSession.invalidate();

        if (scriptSession.isInvalidated())
        {
            return "invalidateMe() succeeded";
        }
        else
        {
            return "invalidateMe() failed";
        }
    }

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(Test.class);
}
