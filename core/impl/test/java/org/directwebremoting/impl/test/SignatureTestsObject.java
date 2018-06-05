package org.directwebremoting.impl.test;

import java.util.List;

/**
 * @author Bram Smeets
 */
public class SignatureTestsObject
{
    /**
     * @return the nos
     */
    public List<Integer> getLotteryResults()
    {
        return nos;
    }

    /**
     * @param nos
     */
    public void setLotteryResults(List<Integer> nos)
    {
        this.nos = nos;
        // do nothing
    }

    private List<Integer> nos;
}
