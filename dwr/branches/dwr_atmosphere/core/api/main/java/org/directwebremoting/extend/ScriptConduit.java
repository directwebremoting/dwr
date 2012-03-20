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
package org.directwebremoting.extend;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;

/**
 * While a Marshaller is processing a request it can register a ScriptConduit
 * with the ScriptSession to say - pass scripts straight to me and bypass the
 * temporary storage area.
 * This interface allows this to happen.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class ScriptConduit implements Comparable<ScriptConduit>
{
    /**
     * All ScriptConduit need a rank
     * @param rank How does this ScriptConduit sort
     * @param holdingConnectionToBrowser Is this conduit a fake one for tracking
     * things, or does it represent a real long term open connection?
     */
    public ScriptConduit(int rank, boolean holdingConnectionToBrowser)
    {
        this.rank = rank;
        this.holdingConnectionToBrowser = holdingConnectionToBrowser;
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
     * Add a script to the list bound for remote execution.
     * <p>It is not an error to refuse to handle the script and return false, it
     * just indicates that this ScriptConduit did not accept the script.
     * If the ScriptConduit can no longer function then it should throw an
     * exception and it will be assumed to be no longer useful.
     * If you want to implement this method then you will probably be doing
     * something like calling {@link ServletOutputStream#print(String)} and
     * passing in the results of calling ScriptBufferUtil.createOutput().
     * @param script The script to execute
     * @return true if this ScriptConduit handled the script.
     * @throws IOException If this conduit is broken and should not be used
     * @throws ConversionException If objects in the script can not be marshalled
     */
    public abstract boolean addScript(ScriptBuffer script) throws IOException, ConversionException;

    /**
     * Is this conduit a fake one for tracking things, or does it represent a
     * real long term open connection?
     */
    public boolean isHoldingConnectionToBrowser()
    {
        return holdingConnectionToBrowser;
    }

    /**
     * @see #isHoldingConnectionToBrowser()
     */
    private boolean holdingConnectionToBrowser;

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @SuppressWarnings({"SubtractionInCompareTo", "NumericCastThatLosesPrecision"})
    public int compareTo(ScriptConduit that)
    {
        int rankdiff = that.getRank() - this.getRank();
        if (rankdiff != 0)
        {
            return rankdiff;
        }

        return (int) (this.id - that.id);
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

        ScriptConduit that = (ScriptConduit) obj;
        return this.id == that.id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @SuppressWarnings({"NumericCastThatLosesPrecision"})
    @Override
    public int hashCode()
    {
        return 17 + (int) id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }

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
        nextId++;
        return nextId;
    }

    /**
     * The next ID, to get around serialization issues
     */
    private static long nextId = 0L;
}
