package uk.ltd.getahead.abc;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Service implements Comparable<Service>
{
    /**
     * @return Returns the billingFreq.
     */
    public char getBillingFreq()
    {
        return billingFreq;
    }

    /**
     * @param billingFreq The billingFreq to set.
     */
    public void setBillingFreq(char billingFreq)
    {
        this.billingFreq = billingFreq;
    }

    /**
     * @return Returns the defaultPrice.
     */
    public int getDefaultPrice()
    {
        return defaultPrice;
    }

    /**
     * @param defaultPrice The defaultPrice to set.
     */
    public void setDefaultPrice(int defaultPrice)
    {
        this.defaultPrice = defaultPrice;
    }

    /**
     * @return Returns the serviceName.
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * @param serviceName The serviceName to set.
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    /**
     * @return Returns the servid.
     */
    public Integer getServid()
    {
        return servid;
    }

    /**
     * @param servid The servid to set.
     */
    public void setServid(Integer servid)
    {
        this.servid = servid;
    }

    /**
     * @return Returns the servLastUpdate.
     */
    public String getServLastUpdate()
    {
        return servLastUpdate;
    }

    /**
     * @param servLastUpdate The servLastUpdate to set.
     */
    public void setServLastUpdate(String servLastUpdate)
    {
        this.servLastUpdate = servLastUpdate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Service[id=" + getServid() + ", name=" + getServiceName() + ", updated=" + getServLastUpdate() + "]";
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

        Service that = (Service) obj;

        if (!this.getServid().equals(that.getServid()))
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
        return getServid().hashCode();
    }

    /**
     * 
     */
    public int compareTo(Service that)
    {
        return this.getServiceName().compareTo(that.getServiceName());
    }

    private Integer servid = -1;

    private String serviceName = "";

    private int defaultPrice = 0;

    private char billingFreq = 'O';

    private String servLastUpdate = null;
}
