package uk.ltd.getahead.abc;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Reminder implements Comparable<Reminder>
{
    /**
     * @return Returns the email.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @return Returns the reminderId.
     */
    public Integer getReminderId()
    {
        return reminderId;
    }

    /**
     * @param reminderId The reminderId to set.
     */
    public void setReminderId(Integer reminderId)
    {
        this.reminderId = reminderId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Service[id=" + getReminderId() + ", name=" + getEmail() + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        Reminder that = (Reminder) obj;

        if (!this.getReminderId().equals(that.getReminderId()))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return getReminderId().hashCode();
    }

    /**
     * 
     */
    public int compareTo(Reminder that)
    {
        return this.getEmail().compareTo(that.getEmail());
    }

    private Integer reminderId = -1;

    private String email = "";
}
