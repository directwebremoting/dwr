package com.example.dwr.people;

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
        String housenum = (random.nextInt(399) + 1) + " ";
        String road1 = ROADS1[random.nextInt(ROADS1.length)];
        String road2 = ROADS2[random.nextInt(ROADS2.length)];
        int townNum = random.nextInt(TOWNS.length);
        String town = TOWNS[townNum];
        return housenum + road1 + " " + road2 + ", " + town;
    }

    public static String[] getAddressAndNumber()
    {
        String[] reply = new String[2];

        String housenum = (random.nextInt(399) + 1) + " ";
        String road1 = ROADS1[random.nextInt(ROADS1.length)];
        String road2 = ROADS2[random.nextInt(ROADS2.length)];
        int townNum = random.nextInt(TOWNS.length);
        String town = TOWNS[townNum];

        reply[0] = housenum + road1 + " " + road2 + ", " + town;
        reply[1] = getPhoneNumber(townNum < 5);

        return reply;
    }

    public static int getAge()
    {
        return random.nextInt(80);
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
        "William", "Jo", "Jane", "Joan", "Jerry", "Jason", "Martin", "Mark",
        "Max", "Mike", "Molly", "Sam", "Shane", "Dwane", "Diane", "Anne", "Anna",
        "Bill", "Jack", "Thomas", "Oliver", "Joshua", "Harry", "Charlie", "Dan",
        "Will", "James", "Alfie", "Grace", "Ruby", "Olivia", "Emily", "Jessica",
        "Sophie", "Chloe", "Lily", "Ella", "Amelia", "Kimberly", "Owen", "Rhys",
        "Layla", "Jonny", "Darren", "Laura", "Bridget", "Carl", "Josie",
    };

    private static final String[] SURNAMES =
    {
        "Sutcliffe", "MacDonald", "Duckworth", "Smith", "Wisner", "Jones",
        "Nield", "Turton", "Trelfer", "Wilson", "Johnson", "Daniels",
        "Jones", "Wilkinson", "Wilton", "Jackson"
    };

    private static final String[] ROADS1 =
    {
        "Amaranth", "Apricot", "Aqua", "Aquamarine", "Beige", "Bronze", "Buff",
        "Burgundy", "Cerise", "Chestnut", "Cobalt", "Coral", "Cream", "Cyan",
        "Denim", "Eggplant", "Fuchsia", "Grey", "Gold", "Indigo", "Ivory",
        "Jade", "Khaki", "Lemon", "Lilac", "Linen", "Magenta", "Magnolia",
        "Maroon", "Mustard", "Ochre", "Olive", "Orange", "Orchid", "Peach",
        "Pear", "Pink", "Ruby", "Scarlet", "Silver", "Sepia", "Tangerine",
        "Taupe", "Tan", "Teal", "Torquise", "Ultramarine", "Violet", "Wheat",
        "Green", "Red", "Yellow", "Brown", "Blue", "Black", "White", "Yellow"
    };

    private static final String[] ROADS2 =
    {
        "Close", "Drive", "Street", "Avenue", "Crescent", "Road", "Place",
        "Way", "Croft", "Lane"
    };

    private static final String[] TOWNS =
    {
        "San Mateo", "San Francisco", "San Diego", "New York", "Atlanta",
        "Sandford", "York", "London", "Coventry", "Exeter", "Knowle", "Rhyl",
        "Stamford",
    };
}
