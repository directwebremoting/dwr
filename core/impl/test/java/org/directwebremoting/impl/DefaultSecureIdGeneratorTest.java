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
        for(int i = 0; i < 2; i++)
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
        for(int i = 0; i < 2; i++)
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
            if (attempts > 10)
            {
                throw new RuntimeException("Can't generate 100 tokens in same millisecond.");
            }

            time1 = System.currentTimeMillis();
            for(int i = 0; i < 100; i++)
            {
                tokenset.add(idGenerator.generate());
            }
            time2 = System.currentTimeMillis();
        } while(time1 != time2);

        assertEquals(100, tokenset.size());

        int countWithoutDash = 0;
        for(String token : tokenset)
        {
            if (!token.contains("-"))
            {
                countWithoutDash++;
            }
        }
        assertTrue("Maximum one token without dash", countWithoutDash <= 1);
    }
}
