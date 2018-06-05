package org.directwebremoting.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class LocalUtilTest
{

    private final Bean zero = new Bean("0");
    private final Bean one = new Bean("1");

    @Test
    public void testCapitalize() {
        assertNull("Null cannot be processed", LocalUtil.capitalize(null));
        assertEquals("No changes", "   null", LocalUtil.capitalize("   null"));
        assertEquals("Letter in upper case", "Null", LocalUtil.capitalize("null"));
    }

    @Test
    public void testGetWriteMethod() throws IntrospectionException
    {
        PropertyDescriptor[] properties = Introspector.getBeanInfo(Bean.class).getPropertyDescriptors();
        assertNotNull("Setter is found for beans", LocalUtil.getWriteMethod(Bean.class, properties[0]));
        Bar<FooImpl> bar = new BarImpl();
        PropertyDescriptor[] barProperties = Introspector.getBeanInfo(bar.getClass()).getPropertyDescriptors();
        assertNull("Introspector alone cannot find the setter", barProperties[1].getWriteMethod());
        assertNotNull("Setter is found for inherit", LocalUtil.getWriteMethod(bar.getClass(), barProperties[1]));
    }

    @Test
    public void testJoin()
    {
        assertNull(LocalUtil.join(null, null));
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, null), "helloworld");
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, ""), "helloworld");
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, "."), "hello.world");
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, ".."), "hello..world");
        assertEquals(LocalUtil.join(new Object[] { zero, one }, "-"), "0-1");
    }

    @Test
    public void testHasText()
    {
        assertFalse(LocalUtil.hasText(""));
        assertFalse(LocalUtil.hasText(null));
        assertFalse(LocalUtil.hasText("   "));
        assertFalse(LocalUtil.hasText(new String()));
        assertTrue(LocalUtil.hasText("hello"));
    }

    @Test
    public void testIsWrapper()
    {
        assertFalse(LocalUtil.isWrapper(null));
        assertFalse(LocalUtil.isWrapper("me"));
        assertTrue(LocalUtil.isWrapper(new Float(2.0)));
    }

    @Test
    public void testIsSafeHierarchicalIdentifierInBrowser() {
        assertTrue(LocalUtil.isSafeHierarchicalIdentifierInBrowser("org.directwebremoting.test"));
        assertFalse(LocalUtil.isSafeHierarchicalIdentifierInBrowser("org.?directwebremoting"));
        assertFalse(LocalUtil.isSafeHierarchicalIdentifierInBrowser("org.<directwebremoting"));
    }

    @Test
    public void testSetProperty() throws Exception
    {
        Bean bean = new Bean("whatever");
        LocalUtil.setProperty(bean, "aux", "new");
        assertEquals("new", bean.getAux());
        LocalUtil.setProperty(bean, "prim", 2);
        assertTrue(bean.getPrim() == 2);
    }

    class Bean
    {
        private String aux;
        private int prim;

        public Bean(String aux)
        {
            this.aux = aux;
        }

        public String getAux()
        {
            return aux;
        }

        public void setAux(String aux)
        {
            this.aux = aux;
        }

        public int getPrim()
        {
            return prim;
        }

        public void setPrim(int prim)
        {
            this.prim = prim;
        }

        @Override
        public String toString()
        {
            return aux;
        }
    }

    interface Foo {}

    class FooImpl implements Foo {}

    interface Bar<T extends Foo>
    {
        T getFoo();
    }

    abstract class AbstractBar<T extends Foo> implements Bar<T>
    {
        T foo;

        public T getFoo()
        {
            return foo;
        }

    }

    class BarImpl extends AbstractBar<FooImpl> {

        protected void setFoo(FooImpl foo)
        {
            this.foo = foo;
        }

    }

}

