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
package org.directwebremoting.ui;

/**
 * A simple wrapper around a String to indicate that this string is executable
 * Javascript and should not be quoted and escaped when it is passed to the
 * client.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CodeBlock
{
    /**
     * CodeBlocks are immutable wrappers around strings
     * @param code The javascript code block to send to the client
     */
    public CodeBlock(String code)
    {
        this.code = code;
    }

    /**
     * Accessor for the (read-only) code block
     * @return The code that this block wraps
     */
    public String getCode()
    {
        return code;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return 397 + code.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        CodeBlock that = (CodeBlock) obj;

        return this.code.equals(that.code);
    }

    /**
     * The code block to send to the client
     */
    private String code;
}
