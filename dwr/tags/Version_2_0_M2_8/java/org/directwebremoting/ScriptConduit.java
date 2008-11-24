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

import java.io.IOException;

import org.directwebremoting.util.LocalUtil;

/**
 * While a Marshaller is processing a request it can register a ScriptConduit
 * with the ScriptSession to say - pass scripts straight to me and bypass the
 * temporary storage area.
 * This interface allows this to happen.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class ScriptConduit implements Comparable
{
    /**
     * All ScriptConduit need a rank
     * @param rank How does this ScriptConduit sort
     */
    public ScriptConduit(int rank)
    {
        this.rank = rank;
    }

    /**
     * ScriptConduits have a rank that indicates the priority order in which we
     * should send scripts to them.
     * The rank is a number from 1 to 10, and should only be one of the defined
     * values: ScriptConduit.RANK_*.
     * @see ScriptConduit#RANK_PROCEDURAL
     * @see ScriptConduit#RANK_FAST
     * @see ScriptConduit#RANK_SLOW
     * @return The rank of this ScriptConduit
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Indicates that this ScriptConduit is used for control-flow and will
     * probably not actually convey the script, but does need to tell someone
     * else about it
     */
    public static final int RANK_PROCEDURAL = 10;

    /**
     * Indicates that this ScriptConduit is a very good way of getting scripts
     * to the client and should be used as a preferred method
     */
    public static final int RANK_FAST = 5;

    /**
     * Indicates that this ScriptConduit is a poor way of getting scripts to the
     * client and should only be used as a last resort.
     */
    public static final int RANK_SLOW = 1;

    /**
     * Called to flush any scripts written to the conduit
     * @throws IOException
     */
    public abstract void flush() throws IOException;

    /**
     * Add a script to the list waiting for remote execution.
     * It is not an error to refuse to handle the script and return false, it
     * just indicates that this ScriptConduit did not accept the script.
     * If the ScriptConduit can no longer function then it should throw an
     * exception and it will be asumed to be no longer useful.
     * @param script The script to execute
     * @return true if this ScriptConduit handled the script.
     * @throws IOException If the script can not go out via this conduit.
     */
    public abstract boolean addScript(String script) throws IOException;

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj)
    {
        ScriptConduit that = (ScriptConduit) obj;

        int rankdiff = this.getRank() - that.getRank();
        if (rankdiff != 0)
        {
            return rankdiff;
        }

        return (int) (this.id - that.id);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return (int) id + 7493;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

        ScriptConduit that = (ScriptConduit) obj;

        return this.id == that.id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        if (classname == null)
        {
            classname = LocalUtil.getShortClassName(getClass());
        }

        return classname + "[id=" + id + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Cached short classname for toString()
     */
    private static String classname = null;

    /**
     * The rank of this ScriptConduit
     */
    private int rank;

    /**
     * Our ID, to get around serialization issues
     */
    private final long id = getNextId();

    /**
     * Get the next unique ID in a thread safe way
     * @return a unique id
     */
    private static synchronized long getNextId()
    {
        return nextId++;
    }

    /**
     * The next ID, to get around serialization issues
     */
    private static long nextId = 0L;
}