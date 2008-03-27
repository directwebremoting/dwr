package uk.ltd.getahead.dwr.test;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Location
{
    /**
     * @param x
     * @param y
     * @param latitude
     * @param longitude
     */
    public Location(int x, int y, float latitude, float longitude)
    {
        this.x = x;
        this.y = y;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 
     */
    public Location()
    {
    }

    /**
     * @return Returns the latitude.
     */
    public float getLatitude()
    {
        return latitude;
    }

    /**
     * @param latitude The latitude to set.
     */
    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    /**
     * @return Returns the longitude.
     */
    public float getLongitude()
    {
        return longitude;
    }

    /**
     * @param longitude The longitude to set.
     */
    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }

    /**
     * @return Returns the x.
     */
    public int getX()
    {
        return x;
    }

    /**
     * @param x The x to set.
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * @return Returns the y.
     */
    public int getY()
    {
        return y;
    }

    /**
     * @param y The y to set.
     */
    public void setY(int y)
    {
        this.y = y;
    }

    private int x;
    private int y;
    private float latitude;
    private float longitude;
}

