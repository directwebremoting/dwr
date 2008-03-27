
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author Rob Sanheim
 *
 * Test OverloadUtil
 * Unless otherwise noted, we cannot assume that isOverloaded(x, y) == isOverloaded(y, x)
 */
public class OverloadUtilTest extends TestCase
{
    private static final String METHOD = "method";

    private static final String ANOTHER_METHOD = "anotherMethod";

    private static final Class[] NO_PARAMETERS = null;

    // fixtures
    private Method returnsStringNoParameters = getMethod(ConcreteClass.class, METHOD, NO_PARAMETERS);

    private Method returnsStringOneParameter = getMethod(ConcreteClass.class, METHOD, String.class);

    private Method returnsObjectNoParametersDifferentName = getMethod(ConcreteClass.class, ANOTHER_METHOD, NO_PARAMETERS);

    private Method returnsStringNoParametersOnSubClass = getMethod(SubClass.class, METHOD, NO_PARAMETERS);

    private Method returnsObjectThreeParametersOnSubClass = getMethod(SubClass.class, METHOD, String.class, String.class, String.class);

    private Method returnsObjectNoParametersOnInterfaceType = getMethod(Interface.class, METHOD, NO_PARAMETERS);

    /**
     * This type of test should be reversible
     *
     * @throws Exception
     */
    public void testTwoSimpleOverloadedMethodsOnSameClass() throws Exception
    {
        assertTrue(OverloadUtil.isOverloaded(returnsStringNoParameters, returnsStringOneParameter));
        assertTrue(OverloadUtil.isOverloaded(returnsStringOneParameter, returnsStringNoParameters));
    }

    public void testMoreSpecificMethodOnConcreteClassOverloadsInterfaceMethods() throws Exception
    {
        assertTrue(OverloadUtil.isOverloaded(returnsObjectNoParametersOnInterfaceType, returnsStringOneParameter));
    }

    public void testInterfaceImplementationIsNotOverloading() throws Exception
    {
        assertFalse(OverloadUtil.isOverloaded(returnsObjectNoParametersOnInterfaceType, returnsStringNoParameters));
    }

    /**
     * This type of test should be reversible
     *
     * @throws Exception
     */
    public void testIsNotOverloadingForDifferentNamesOnSameClass() throws Exception
    {
        assertFalse(OverloadUtil.isOverloaded(returnsStringNoParameters, returnsObjectNoParametersDifferentName));
        assertFalse(OverloadUtil.isOverloaded(returnsStringOneParameter, returnsObjectNoParametersDifferentName));
    }

    public void testIsNotOverloadingForDifferentNamesInterfaceAgainstConcrete() throws Exception
    {
        assertFalse(OverloadUtil.isOverloaded(returnsObjectNoParametersOnInterfaceType, returnsObjectNoParametersDifferentName));
    }

    public void testOverridingIsNotOverloading() throws Exception
    {
        assertFalse(OverloadUtil.isOverloaded(returnsStringNoParameters, returnsStringNoParametersOnSubClass));
    }

    public void testMethodOnSubclassOverloadsInterfaceMethod() throws Exception
    {
        assertTrue(OverloadUtil.isOverloaded(returnsObjectNoParametersOnInterfaceType, returnsObjectThreeParametersOnSubClass));
    }

    /**
     * This type of test should be reversible
     *
     * @throws Exception
     */
    public void testMethodOnSubclassOverloadsMethodOnSameClass() throws Exception
    {
        assertTrue(OverloadUtil.isOverloaded(returnsStringNoParametersOnSubClass, returnsObjectThreeParametersOnSubClass));
        assertTrue(OverloadUtil.isOverloaded(returnsObjectThreeParametersOnSubClass, returnsStringNoParametersOnSubClass));
    }

    public void testMethodOnSubclassOverloadsMethodOnSuperClass() throws Exception
    {
        assertTrue(OverloadUtil.isOverloaded(returnsStringOneParameter, returnsObjectThreeParametersOnSubClass));
    }

    public void testMethodOnSubclassDoesNotOverloadMethodOfDifferentNameOnSuperClass() throws Exception
    {
        assertFalse(OverloadUtil.isOverloaded(returnsObjectNoParametersDifferentName, returnsObjectThreeParametersOnSubClass));
    }

    /**
     * Util method to get a method, ignoring checked exceptions
     *
     * @param clazz
     * @param name
     * @param parameterTypes
     * @return method
     */
    private static Method getMethod(Class clazz, String name, Class... parameterTypes)
    {
        Method method = null;
        try
        {
            method = clazz.getMethod(name, parameterTypes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return method;
    }

}

interface Interface
{
    public Object method();
}

class ConcreteClass implements Interface
{

    /** (non-Javadoc)
     * implements the method in the interface type with a covariant return type
     * @see com.robsanheim.sandbox.reflection.overloading.Interface#method()
     */
    public String method()
    {
        return null;
    }

    /**
     * Overloads method in this concrete class, does not overload method from interface
     * @param one
     * @return
     */
    public String method(String one)
    {
        return null;
    }

    public Object anotherMethod()
    {
        return null;
    }
}

class SubClass extends ConcreteClass
{
    /**
     * Override method from concrete class
     */
    public String method()
    {
        return null;
    }

    public Object method(String one, String two, String three)
    {
        return null;
    }
}
