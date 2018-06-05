package org.directwebremoting.impl;

import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.impl.test.TestCreatedObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCreatorManagerTest
{
    private final DefaultCreatorManager manager = new DefaultCreatorManager();

    @Test
    public void addCreatorTypeNull()
    {
        int before = manager.creatorTypes.size();
        manager.addCreatorType(null, null);
        manager.addCreatorType(null, this.getClass().getName());
        manager.addCreatorType(null, Creator.class.getName());
        int after = manager.creatorTypes.size();
        Assert.assertEquals(before, after);
    }

    @Test(expected = Exception.class)
    public void addCreatorTypeFail2()
    {
        manager.addCreatorType("foo", null);
    }

    @Test
    public void addCreatorType()
    {
        manager.addCreatorType("foo", NewCreator.class.getName());
    }

    @Test
    public void addCreator()
    {
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        manager.addCreator(creator);
    }
}
