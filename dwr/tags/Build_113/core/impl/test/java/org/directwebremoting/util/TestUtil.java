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
package org.directwebremoting.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestUtil
{
    /**
     * Parse the list of tests to detect json test that we need to run
     * @param filename The name of a file in the same package
     * @return A list of json test, read from the file
     * @throws IOException
     */
    public static List<String> parseTestInput(Class<?> base, String filename) throws IOException
    {
        InputStream raw = base.getResourceAsStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(raw));
        List<String> tests = new ArrayList<String>();
        StringBuilder test = new StringBuilder();

        while (true)
        {
            String line = in.readLine();

            if (line == null)
            {
                break;
            }

            String trim = line.trim();

            if (trim.startsWith("#"))
            {
                continue;
            }

            if (trim.length() != 0)
            {
                test.append(line);
            }
            else
            {
                if (test.toString().trim().length() != 0)
                {
                    tests.add(test.toString());
                    test = new StringBuilder();
                }
            }
        }

        if (test.toString().trim().length() != 0)
        {
            tests.add(test.toString());
            test = new StringBuilder();
        }

        return tests;
    }
}
