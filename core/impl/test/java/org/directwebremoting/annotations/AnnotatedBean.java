package org.directwebremoting.annotations;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@DataTransferObject
public class AnnotatedBean extends AnnotatedBeanSuper
{
    @RemoteProperty
    private  String convertMe;

    private String doNotConvertMe;

    /**
     * @return the convertMe
     */
    public String getConvertMe()
    {
        return convertMe;
    }
    /**
     * @param convertMe the convertMe to set
     */
    public void setConvertMe(String convertMe)
    {
        this.convertMe = convertMe;
    }
    /**
     * @return the doNotConvertMe
     */
    public String getDoNotConvertMe()
    {
        return doNotConvertMe;
    }
    /**
     * @param doNotConvertMe the doNotConvertMe to set
     */
    public void setDoNotConvertMe(String doNotConvertMe)
    {
        this.doNotConvertMe = doNotConvertMe;
    }

}

