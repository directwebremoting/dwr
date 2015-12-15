
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Rob Sanheim
 *
 * A utility to check if one method is overloading another. Note that this
 * utility does not take into account generics at all, but it should work
 * correctly for covariant return types.
 * @link <a *       href="http://java.sun.com/docs/books/jls/third_edition/html/classes.html#227768">the
 *       JLS</a> for information on overloading
 *
 * TODO change to allow parameters to isOverloaded be ordered any way, and make this
 *           class figure out which is the "higher level" method
 *
 * TODO change to support generics, particularily the nasty case where an
 *           overloaded or overridden method uses generics and the base method does not -
 *           see <a *         href="http://java.sun.com/docs/books/jls/third_edition/html/classes.html#8.4.8.3">JLS
 *           again</a>
 */
public class OverloadUtil
{
    /**
     * Check two methods reflectively to see if one is overloading the other
     * Note that the parameter ordering is important if one method is higher in
     * the class hiearchy then the other. If this is the case, make sure the
     * method higher up in the chain is the first parameter. Otherwise, the
     * ordering does not matter.
     *
     * @param higher
     *            method
     * @param lower
     *            method
     * @return
     */
    public static boolean isOverloaded(Method higher, Method lower)
    {
        if (namesAreEqual(higher, lower) && returnTypesAreEqualOrCovariant(higher, lower) && isNotInterfaceImplementation(higher, lower)
            && isNotOverridden(higher, lower))
        {
            return true;

        }
        else
        {
            return false;
        }
    }

    private static boolean isNotOverridden(Method higher, Method lower)
    {
        if (isOverridden(higher, lower))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * @param higher
     * @param lower
     * @return true if lower overrides higher
     */
    private static boolean isOverridden(Method higher, Method lower)
    {
        return declaringClassIsAssignableFrom(higher, lower) && declaringClassIsNotAnInterface(higher) && parametersAreEqual(higher, lower);
    }

    /**
     * @param first
     * @param second
     * @return true if the first method's declaring class is assignable from the
     *         second
     */
    private static boolean declaringClassIsAssignableFrom(Method first, Method second)
    {
        return first.getDeclaringClass().isAssignableFrom(second.getDeclaringClass());

    }

    /**
     * We have to make sure we don't mistake standard interface implementation
     * (where first method is on an interface and the params are equal) for
     * overloading.
     *
     * @param higher
     * @param lower
     * @return
     */
    private static boolean isNotInterfaceImplementation(Method higher, Method lower)
    {
        return !(declaringClassIsAnInterface(higher) && parametersAreEqual(higher, lower));
    }

    /**
     * check deep equality on parameters of two methods
     *
     * @param first
     * @param second
     * @return
     */
    private static boolean parametersAreEqual(Method first, Method second)
    {
        return Arrays.deepEquals(first.getParameterTypes(), second.getParameterTypes());
    }

    /**
     * @param higher
     * @param lower
     * @return true if return types are equal or covariants
     */
    private static boolean returnTypesAreEqualOrCovariant(Method higher, Method lower)
    {
        return (declaringClassIsAssignableFrom(higher, lower) || higher.getReturnType().equals(lower.getReturnType()));
    }

    /**
     * @param first
     * @param second
     * @return true if the names of the two methods are equal
     */
    private static boolean namesAreEqual(Method first, Method second)
    {
        return first.getName().equals(second.getName());
    }

    private static boolean declaringClassIsAnInterface(Method method)
    {
        return method.getDeclaringClass().isInterface();
    }

    private static boolean declaringClassIsNotAnInterface(Method method)
    {
        return !declaringClassIsAnInterface(method);
    }

}
