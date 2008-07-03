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
package org.directwebremoting.spring;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.create.test.DummyDataManager;
import org.directwebremoting.extend.Creator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.StaticApplicationContext;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringCreatorTest
{
    private SpringCreator creator;

    @Before
    public void setUp() throws Exception
    {
        creator = new SpringCreator();

        Map<String, String> params = new HashMap<String, String>();
        params.put("location", "/uk/ltd/getahead/dwr/create/spring-beans.xml");
        creator.setProperties(params);

        creator.setBeanName("dataManager");
    }

    @Test
    public void testBeanName()
    {
        String beanName = "beanName";
        creator.setBeanName(beanName);
        Assert.assertEquals(beanName, creator.getBeanName());
    }

    @Test
    public void testGetScope()
    {
        // make sure the default scope is the PAGE scope
        Assert.assertEquals(Creator.PAGE, creator.getScope());
    }

    @Ignore
    @Test
    public void testGetType()
    {
        Assert.assertEquals(DummyDataManager.class, creator.getType());
    }

    @Ignore
    @Test
    public void testGetInstance() throws Exception
    {
        DummyDataManager mgr = (DummyDataManager) creator.getInstance();
        Assert.assertEquals(new DummyDataManager(), mgr);
    }

    @Ignore
    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testNonExistingBean() throws Exception
    {
        creator.setBeanName("nonExistingBean");
        creator.getInstance();
    }

    @Test
    public void testOverrideBeanFactory() throws Exception
    {
        StaticApplicationContext ctx = new StaticApplicationContext();
        ctx.registerSingleton("dataManager", DummyDataManager.class);
        SpringCreator.setOverrideBeanFactory(ctx);

        Assert.assertEquals(DummyDataManager.class, creator.getType());

        DummyDataManager mgr = (DummyDataManager) creator.getInstance();
        Assert.assertEquals(new DummyDataManager(), mgr);

        SpringCreator.setOverrideBeanFactory(null);
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void testWithoutBeanNameWithRequest() throws Exception
    {
        SpringCreator creator2 = new SpringCreator();
        creator2.setBeanName("dataManager");
        creator2.getInstance();
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void testWithoutBeanNameWithoutRequest() throws Exception
    {
        SpringCreator creator2 = new SpringCreator();
        creator2.setBeanName("dataManager");
        creator2.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetClass() throws Exception
    {
        SpringCreator creator2 = new SpringCreator();
        creator2.setClass(getClass().getName());
        creator2.setClass("NonExistingClass");
    }
}
