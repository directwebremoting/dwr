package uk.ltd.getahead.dwr.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;

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
}
