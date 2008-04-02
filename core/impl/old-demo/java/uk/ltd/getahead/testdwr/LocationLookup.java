package uk.ltd.getahead.testdwr;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Mapping demo.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LocationLookup
{
    /**
     * Find a location from a postcode
     * @param postcode
     * @return The location of the postcode or null if no location was found
     */
    public Location findLocation(String postcode)
    {
        if (postcode == null)
        {
            throw new NullPointerException();
        }

        if (postcode.length() == 0)
        {
            return null;
        }

        postcode = postcode.toLowerCase();

        if (postcode.length() > 4)
        {
            postcode = postcode.substring(0, 4);
        }

        Location reply = locations.get(postcode);
        if (reply == null)
        {
            postcode = postcode.substring(0, 3);
            reply = locations.get(postcode);
        }

        return reply;
    }

    private static Map<String, Location> locations = new HashMap<String, Location>();

    static
    {
        try
        {
            InputStream in = LocationLookup.class.getResourceAsStream("map.properties"); //$NON-NLS-1$
            Properties prop = new Properties();
            prop.load(in);
            for (Iterator<?> it = prop.keySet().iterator(); it.hasNext();)
            {
                String key = (String) it.next();
                String value = prop.getProperty(key);

                StringTokenizer st = new StringTokenizer(value, ","); //$NON-NLS-1$
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                float lat = Float.parseFloat(st.nextToken());
                float lon = Float.parseFloat(st.nextToken());

                Location loc = new Location(x, y, lat, lon);
                locations.put(key.toLowerCase(), loc);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
