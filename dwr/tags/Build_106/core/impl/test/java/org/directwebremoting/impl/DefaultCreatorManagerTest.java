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
package org.directwebremoting.impl;

import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.Creator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCreatorManagerTest
{
    private DefaultCreatorManager manager = new DefaultCreatorManager();

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
        manager.addCreator(null, creator);
    }
}
