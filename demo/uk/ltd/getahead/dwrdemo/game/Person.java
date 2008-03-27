package uk.ltd.getahead.dwrdemo.game;

/**
 * A player of battleships
 */
public class Person
{
    /**
     * Create a new player
     */
    public Person()
    {
        setName(BattleshipUtil.getRandomCaptainName());
        setColor(BattleshipUtil.getRandomLightHtmlColorString());
        setPosition(new Position());
        setScore(0);
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position)
    {
        this.position = position;
    }

    /**
     * @return the position
     */
    public Position getPosition()
    {
        return position;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * @return the color
     */
    public String getColor()
    {
        return color;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score)
    {
        this.score = score;
    }

    /**
     * @return the score
     */
    public int getScore()
    {
        return score;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "<span style='color:" + color + ";'>" + name + "</span>";
    }

    private String name;
    private Position position;
    private String color;
    private int score;
}