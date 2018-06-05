package org.directwebremoting.create.test;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bram Smeets
 */
public class DummyDataManager extends DummyManagerParent
{
    @Override
    public void doTest()
    {
        // do nothing
    }

    public void doTest(String s)
    {
        String ignore = s; s = ignore;
        // do nothing
    }

    public void debugger()
    {
        // do nothing
    }

    @SuppressWarnings("unused")
    public String testArguments(String s, int i, long l, boolean b, float f, List<?> list, Map<?, ?> map, HttpServletRequest request)
    {
        return "testString";
    }

    public void doException() throws Exception
    {
        throw new Exception("testing");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (obj != null && obj instanceof DummyDataManager)
        {
            //DummyDataManager mgr = (DummyDataManager) obj;
            return true;
        }
        return false;
    }
}
