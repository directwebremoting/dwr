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
package org.directwebremoting;

/**
 * Reply is a read-only POJO to encapsulate the information required to make a
 * single java call, including the result of the call (either returned data or
 * exception).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Reply
{
    /**
     * Constructor for the success case.
     * @param id The call id, copied from the Call object
     * @param reply The successful reply data
     */
    public Reply(String id, Object reply)
    {
        this.id = id;
        this.reply = reply;
    }

    /**
     * Constructor for the error case.
     * Reply <b>must</b> be set to null for this constructor to work. This
     * parameter exists to avoid overloading issues. See Java Puzzlers #46 for
     * an example.
     * @param id The call id, copied from the Call object
     * @param reply Must be set to null
     * @param th The exception to record against this call.
     */
    public Reply(String id, Object reply, Throwable th)
    {
        if (reply != null)
        {
            throw new NullPointerException("'reply' must be null when setting an Exception."); //$NON-NLS-1$
        }

        this.id = id;
        this.th = th;
    }

    /**
     * @return Returns the call id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return Returns the call return value.
     */
    public Object getReply()
    {
        return reply;
    }

    /**
     * @return Returns the Exception
     */
    public Throwable getThrowable()
    {
        return th;
    }

    private String id = null;

    private Object reply = null;

    private Throwable th = null;
}
