package org.directwebremoting.impl;

import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public class DefaultSecureIdGeneratorTest
{
    private final DefaultSecureIdGenerator idGenerator = new DefaultSecureIdGenerator();

    /**
     * Test a large number of generations
     * @throws Exception
     */
    @Test
    public void testMany() throws Exception
    {
        HashSet<String> tokenset = new HashSet<String>();
        for(int i = 0; i < 1000; i++)
        {
            for(int j = 0; j < 100; j++)
            {
                tokenset.add(idGenerator.generate());
            }
            Thread.sleep(1);
        }

        assertEquals(100*1000, tokenset.size());
    }

    /**
     * Generate 100 tokens during different milliseconds
     * @throws Exception
     */
    @Test
    public void testDifferentMilliseconds() throws Exception
    {
        HashSet<String> tokenset = new HashSet<String>();
        for(int i = 0; i < 100; i++)
        {
            tokenset.add(idGenerator.generate());
            long time1 = System.currentTimeMillis();
            long time2;
            do
            {
                Thread.sleep(1);
                time2 = System.currentTimeMillis();
            } while(time1 == time2);
        }

        assertEquals(100, tokenset.size());

        for(String token : tokenset)
        {
            assertTrue("Tokens should not contain a dash (-)", !token.contains("-"));
        }
    }

    /**
     * Generate 100 tokens during same millisecond
     * @throws Exception
     */
    @Test
    public void testSameMillisecond() throws Exception
    {
        HashSet<String> tokenset = new HashSet<String>();
        long time1;
        long time2;
        int attempts = 0;
        do
        {
            attempts++;
            tokenset.clear();

            time1 = System.currentTimeMillis();
            for(int i = 0; i < 10; i++)
            {
                tokenset.add(idGenerator.generate());
            }
            time2 = System.currentTimeMillis();
        } while(time1 != time2 && attempts < 100);

        int countWithoutDash = 0;
        for(String token : tokenset)
        {
            if (!token.contains("-"))
            {
                countWithoutDash++;
            }
        }
        if (attempts < 100)
        {
            assertTrue("Maximum one token without dash", countWithoutDash <= 1);
        }
    }
}
