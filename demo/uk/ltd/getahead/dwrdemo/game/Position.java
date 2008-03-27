package uk.ltd.getahead.dwrdemo.game;

import java.util.Random;

/**
 * A board position
 */
public class Position
{
    private int row;
    private int col;

    /**
     * @param row
     * @param col
     */
    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    /**
     * 
     */
    public Position()
    {
        setRow(random.nextInt(MAX_ROW));
        setCol(random.nextInt(MAX_COL));
    }

    /**
     * @param col the col to set
     */
    public void setCol(int col)
    {
        if (col >= MAX_COL)
        {
            throw new IllegalArgumentException("Column to big");
        }

        if (col < 0)
        {
            throw new IllegalArgumentException("Column to small");
        }

        this.col = col;
    }

    /**
     * @return the col
     */
    public int getCol()
    {
        return col;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row)
    {
        if (row >= MAX_ROW)
        {
            throw new IllegalArgumentException("Row to big");
        }

        if (row < 0)
        {
            throw new IllegalArgumentException("Row to small");
        }

        this.row = row;
    }

    /**
     * @return the row
     */
    public int getRow()
    {
        return row;
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        Position that = (Position) obj;

        return this.getRow() == that.getRow() && this.getCol() == that.getCol();
    }

    public int hashCode()
    {
        return getRow() * getCol();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "(" + row + "," + col + ")";
    }

    /**
     * The max number of rows in the grid
     */
    protected static final int MAX_ROW = 7;

    /**
     * The max number of columns in the grid
     */
    protected static final int MAX_COL = 8;

    /**
     * How big is the grid
     */
    public static final Position FULL_GRID = new Position(MAX_ROW, MAX_COL);

    /**
     * A random number generator
     */
    protected static Random random = new Random();
}
