package uk.ltd.getahead.dwr.test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.util.Log;

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
     * @return string
     */
    // @PMD:REVIEWED:LooseCoupling: by Joe on 09/02/05 08:25
    public HashSet stringHashSetParam(HashSet test)
    {
        return test;
    }

    /**
     * @param test
     * @return string
     */
    // @PMD:REVIEWED:LooseCoupling: by Joe on 09/02/05 08:25
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
     * @param data The results of the current test
     * @return A summary of all the received results
     */
    public Map reply(Map data)
    {
        String failReport = (String) data.get("failreport"); //$NON-NLS-1$

        HttpServletRequest request = ExecutionContext.get().getHttpServletRequest();
        String userAgentHttp = request.getHeader("User-Agent"); //$NON-NLS-1$
        userAgentHttp = simplfyUserAgent(userAgentHttp);
        if (userAgentHttp.length() > 100)
        {
            userAgentHttp = userAgentHttp.substring(0, 100);
        }

        if (log == null)
        {
            try
            {
                String home = System.getProperty("user.home"); //$NON-NLS-1$
                Writer out = new FileWriter(home + File.separator + "test.log", true); //$NON-NLS-1$
                // URL url = ExecutionContext.get().getServletContext().getResource("/test.log"); //$NON-NLS-1$
                // OutputStream out = url.openConnection().getOutputStream();
                log = new PrintWriter(out);
            }
            catch (Exception ex)
            {
                Log.error("Failed to open test log file", ex); //$NON-NLS-1$
            }
        }

        if (log != null)
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
                log.write("\n" + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$                
            }
            log.write("\nuseragent-http=" + userAgentHttp); //$NON-NLS-1$
            log.write("\naddr=" + request.getRemoteAddr()); //$NON-NLS-1$
            log.write("\n"); //$NON-NLS-1$
            log.flush();
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
    public String dangerOverride(String param1)
    {
        return "Test.dangerOverride(" + param1 + ") says hello.";  //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * @return string
     */
    public String dangerOverride()
    {
        return "Test.dangerOverride() says hello."; //$NON-NLS-1$
    }

    private static PrintWriter log = null;

    private static Map results = new HashMap();
}
