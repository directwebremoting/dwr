package org.directwebremoting.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.IdGenerator;
import org.directwebremoting.util.Base64;

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

        // Let SecureRandom do its own secure initialization before we add our seed
        random.nextBytes(new byte[1]);

        // Now seed the generator
        reseed();
    }

    /**
     * Generates an id string guaranteed to be unique for eternity within
     * the scope of the running server, as long as the real-time clock is
     * not adjusted backwards. The generated string consists of alphanumerics
     * (A-Z, a-z, 0-9) and symbols !, ~ and -.
     * @return A unique id string
     * @see org.directwebremoting.extend.IdGenerator#generate()
     */
    public synchronized String generate()
    {
        reseedIfNeeded();

        StringBuilder idbuf = new StringBuilder();

        // New cookie RFC 6265 says the following non-alphanumeric chars are allowed
        // in cookie values: %x21 / %x23-2B / %x2D-3A / %x3C-5B / %x5D-7E, which
        // corresponds to:
        //   !#$%&'()*+-./:<=>?@[]^_`{|}~
        // We avoid the following as they have special meaning in URLs:
        //   #%&+?
        // We avoid the following as we are using them as separators ourselves:
        //   /-
        // We avoid these as they were discouraged in older cookie specs:
        //   ():=@[]{}
        // We avoid these as they have special meaning in regexps:
        //   $*.^|
        // These remain:
        //   !'<>_`~
        // And we have chosen these as our two "base64" special chars:
        //   !~

        // Generate 21 random bytes (168 bits) and add as 28 printable 6-bit bytes
        final byte[] bytes = new byte[21];
        random.nextBytes(bytes);
        String base64 = new String(Base64.encodeBase64(bytes));
        String base64Adjusted = base64.replaceAll("\\+", "!").replaceAll("/", "~");
        idbuf.append(base64Adjusted);

        // Second part of the id string is the 64 bit timestamp converted
        // into as many 6 bit lookup chars as needed (variable length)
        long time = System.currentTimeMillis();
        long remainder = time;
        final char[] charmap = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!~".toCharArray();
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

        count++;
        countSinceSeed++;
        countSinceTimeChange++;
        lastGenTime = time;
        String id = idbuf.toString();
        lastHashCode = System.identityHashCode(id);
        return id;
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

        try {
            // Make up a base for entropy
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream data = new DataOutputStream(os);
            data.writeLong(System.nanoTime());
            byte[] prngOutput = new byte[128];
            random.nextBytes(prngOutput);
            data.write(prngOutput);
            data.write(count);
            data.write(lastHashCode);
            os.close();
            byte[] base = os.toByteArray();

            // Hash and pick out first 128 bits
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base);
            byte[] hash128 = new byte[16];
            System.arraycopy(hash, 0, hash128, 0, 16);

            // Reseed
            log.debug("Reseeding with " + Arrays.toString(hash128));
            random.setSeed(hash128);
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
        catch(NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * The random number source
     */
    protected SecureRandom random = null;

    /**
     * Number of ids generated since startup
     */
    protected int count = 0;

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

    /**
     * Hashcode from last id generation
     */
    protected int lastHashCode = 0;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultSecureIdGenerator.class);
}
