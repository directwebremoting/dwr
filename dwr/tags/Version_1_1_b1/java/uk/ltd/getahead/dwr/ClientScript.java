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
package uk.ltd.getahead.dwr;

/**
 * A ClientScript contains a clip of Javascript designed for execution on a
 * client web-browser.
 * 
 * <p>ClientScript is basically a wrapper around a String. But we use the
 * wrapper rather than a String so we have clear rules on identity. With Strings
 * interning can mean that to strings with identical contents are equal() when
 * they are not ==. With ClientScript the equal() rules are ==.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ClientScript
{
    /**
     * Immutable constructor
     * @param script The script to execute on the client
     */
    public ClientScript(String script)
    {
        this.script = script;
    }

    /**
     * @return Returns the script to execute on the client.
     */
    public String getScript()
    {
        return script;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        return obj == this;
    }

    /**
     * The script to execute on the client
     */
    private String script;
}
