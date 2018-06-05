package org.directwebremoting.create;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NewCreatorTest
{
    @Test
    public void test() throws Exception
    {
        NewCreator creator = new NewCreator();
        creator.setClass(getClass().getName());

        Assert.assertEquals(getClass(), creator.getType());
        Assert.assertTrue(creator.getInstance() instanceof NewCreatorTest);
    }
}
