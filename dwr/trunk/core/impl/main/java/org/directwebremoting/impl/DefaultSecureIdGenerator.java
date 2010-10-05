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

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import org.directwebremoting.extend.IdGenerator;

/**
 * An id generator that generates secure (non-predictable) random strings
 * that are guaranteed to be unique for eternity within the scope of the
 * running server, as long as the real-time clock is not adjusted backwards.
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public class DefaultSecureIdGenerator implements IdGenerator
{
    public DefaultSecureIdGenerator()
    {
        // SecureRandom implements a cryptographically secure pseudo-random
        // number generator (PRNG).
        // We want Sun's SHA1 algorithm on all platforms.
        // (see http://www.cigital.com/justiceleague/2009/08/14/proper-use-of-javas-securerandom/)

        // Try Sun's SHA1
        try
        {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        }
        catch (NoSuchAlgorithmException ex) { /* squelch */ }
        catch (NoSuchProviderException ex) { /* squelch */ }

        // Try any SHA1
        try
        {
            if (random == null)
            {
                random = SecureRandom.getInstance("SHA1PRNG");
            }
        }
        catch (NoSuchAlgorithmException ex) { /* squelch */ }

        // Fall back to default
        if (random == null)
        {
            random = new SecureRandom();
        }

        // Now seed the generator
        reseed();
    }

    /**
     * Generates an id string guaranteed to be unique for eternity within
     * the scope of the running server, as long as the real-time clock is
     * not adjusted backwards. The generated string consists of alphanumerics
     * (A-Z, a-z, 0-9) and symbols *, $ and -.
     * @return A unique id string
     * @see org.directwebremoting.extend.IdGenerator#generate()
     */
    public synchronized String generate()
    {
        reseedIfNeeded();

        // Generate 20 random bytes (160 bits)
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        // 64 character lookup table (= 2^6, 6 bits)
        final char[] charmap = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ*$".toCharArray();

        // First part of the id string is the lookup char of the lower 6
        // bits of each of the random bytes (20 bytes)
        StringBuilder idbuf = new StringBuilder();
        for (byte b : bytes)
        {
            idbuf.append(charmap[b & 0x3F]);
        }

        // Second part of the id string is the 64 bit timestamp converted
        // into as many 6 bit lookup chars as needed (variable length)
        long time = System.currentTimeMillis();
        long remainder = time;
        while (remainder > 0)
        {
            idbuf.append(charmap[(int) remainder & 0x3F]);
            remainder = remainder >>> 6;
        }

        // If we have generated other ids during the same millisecond (same
        // millisecond could mean an up to 50 msec interval on some platforms
        // due to a coarse timer resolution) then ensure that we have no
        // collisions ...
        if (time == lastGenTime)
        {
            // Add a third delimited section (delimiter needed to avoid
            // collisions due to sections two and three being of variable
            // length) with an incremented number mapped to lookup chars
            idbuf.append('-'); // delimiter
            remainder = countSinceTimeChange;
            while (remainder > 0)
            {
                idbuf.append(charmap[(int) remainder & 0x3F]);
                remainder = remainder >>> 6;
            }
        }
        // ... otherwise reset to prepare the new millisecond
        else
        {
            countSinceTimeChange = 0;
        }

        countSinceSeed++;
        countSinceTimeChange++;
        lastGenTime = time;
        return idbuf.toString();
    }

    /**
     * Trigger reseed at desired intervals.
     */
    protected void reseedIfNeeded()
    {
        boolean needReseed = false;

        // Reseed if more than 15 minutes have passed since last reseed
        long time = System.currentTimeMillis();
        if (time - seedTime > 15 * 60 * 1000)
        {
            needReseed = true;
        }

        // Reseed if more than 1000 ids have been generated
        if (countSinceSeed > 1000)
        {
            needReseed = true;
        }

        if (needReseed)
        {
            reseed();

            // Update bookkeeping
            seedTime = time;
            countSinceSeed = 0;
        }
    }

    /**
     * Set up entropy in random number generator
     */
    protected void reseed()
    {
        // We would really like to reseed using:
        //   random.setSeed(random.generateSeed(20));
        // to get 160 bits (SHA1 width) truly random data, but as most
        // Linuxes don't come configured with the driver for the Intel
        // hardware RNG, this usually blocks the whole server...

        // Reseed using nano time.
        random.setSeed(System.nanoTime());
    }

    /**
     * The random number source
     */
    protected SecureRandom random = null;

    /**
     * Number of ids generated since last seeding of random source
     */
    protected int countSinceSeed = 0;

    /**
     * Timestamp from last seeding of random source
     */
    protected long seedTime = 0;

    /**
     * Number of ids generated during the last same millisecond
     */
    protected int countSinceTimeChange = 0;

    /**
     * Timestamp from last id generation
     */
    protected long lastGenTime = 0;
}
