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
package org.getahead.dwrdemo.util;

import java.util.Random;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class RandomData
{
    /**
     * @param isUS US numbers look different to UK ones
     * @return A phone number
     */
    public static String getPhoneNumber(boolean isUS)
    {
        String phoneNumber;
        if (isUS)
        {
            // US
            phoneNumber = "+1 (" + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + ") "
                + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + " - "
                + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9);
        }
        else
        {
            // UK
            phoneNumber = "+44 (0) 1" + random.nextInt(9) + random.nextInt(9) + random.nextInt(9)
                + " " + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + random.nextInt(9)
                + random.nextInt(9) + random.nextInt(9);
        }
        return phoneNumber;
    }

    public static String getFirstName()
    {
        return FIRSTNAMES[random.nextInt(FIRSTNAMES.length)];
    }

    public static String getSurname()
    {
        return SURNAMES[random.nextInt(SURNAMES.length)];
    }

    public static String getFullName()
    {
        return getFirstName() + " " + getSurname();
    }

    public static String getAddress()
    {
        String housenum = (random.nextInt(99) + 1) + " ";
        String road1 = ROADS1[random.nextInt(ROADS1.length)];
        String road2 = ROADS2[random.nextInt(ROADS2.length)];
        int townNum = random.nextInt(TOWNS.length);
        String town = TOWNS[townNum];
        return housenum + road1 + " " + road2 + ", " + town;
    }

    public static String[] getAddressAndNumber()
    {
        String[] reply = new String[2];

        String housenum = (random.nextInt(99) + 1) + " ";
        String road1 = ROADS1[random.nextInt(ROADS1.length)];
        String road2 = ROADS2[random.nextInt(ROADS2.length)];
        int townNum = random.nextInt(TOWNS.length);
        String town = TOWNS[townNum];

        reply[0] = housenum + road1 + " " + road2 + ", " + town;
        reply[1] = getPhoneNumber(townNum < 5);

        return reply;
    }

    public static float getSalary()
    {
        return Math.round(10 + 90 * random.nextFloat()) * 1000;
    }

    private static final Random random = new Random();

    private static final String[] FIRSTNAMES =
    {
        "Fred", "Jim", "Shiela", "Jack", "Betty", "Jacob", "Martha", "Kelly",
        "Luke", "Matt", "Gemma", "Joe", "Ben", "Jessie", "Leanne", "Becky",
        "William", "Jo"
    };

    private static final String[] SURNAMES =
    {
        "Sutcliffe", "MacDonald", "Duckworth", "Smith", "Wisner", 
        "Nield", "Turton", "Trelfer", "Wilson", "Johnson", "Daniels",
        "Jones", "Wilkinson", "Wilton"
    };

    private static final String[] ROADS1 =
    {
        "Green", "Red", "Yellow", "Brown", "Blue", "Black", "White",
    };

    private static final String[] ROADS2 =
    {
        "Close", "Drive", "Street", "Avenue", "Crescent", "Road", "Place",
    };

    private static final String[] TOWNS =
    {
        "San Mateo", "San Francisco", "San Diego", "New York", "Atlanta",
        "Sandford", "York", "London", "Coventry", "Exeter", "Knowle",
    };
}

