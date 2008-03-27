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
package uk.ltd.getahead.testdwr;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestBean
{
    /**
     * 
     */
    public TestBean()
    {
    }

    /**
     * @param string
     * @param integer
     * @param testBean
     */
    public TestBean(int integer, String string, TestBean testBean)
    {
        this.string = string;
        this.integer = integer;
        this.testBean = testBean;
    }

    /**
     * @return Returns the integer.
     */
    public int getInteger()
    {
        return integer;
    }

    /**
     * @param integer The integer to set.
     */
    public void setInteger(int integer)
    {
        this.integer = integer;
    }

    /**
     * @return Returns the string.
     */
    public String getString()
    {
        return string;
    }

    /**
     * @param string The string to set.
     */
    public void setString(String string)
    {
        this.string = string;
    }

    /**
     * @return Returns the testBean.
     */
    public TestBean getTestBean()
    {
        return testBean;
    }

    /**
     * @param testBean The testBean to set.
     */
    public void setTestBean(TestBean testBean)
    {
        this.testBean = testBean;
    }

    private String string = "Default initial value"; //$NON-NLS-1$
    private int integer = 0;
    private TestBean testBean = null;
}
