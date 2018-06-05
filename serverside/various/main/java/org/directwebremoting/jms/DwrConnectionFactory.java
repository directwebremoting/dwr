package org.directwebremoting.jms;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An implementation of {@link ConnectionFactory} for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrConnectionFactory implements ConnectionFactory
{
	/* (non-Javadoc)
	 * @see javax.jms.ConnectionFactory#createConnection()
	 */
	public DwrConnection createConnection() throws JMSException
	{
		return new DwrConnection();
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionFactory#createConnection(java.lang.String, java.lang.String)
	 */
	public DwrConnection createConnection(String username, String password) throws JMSException
	{
	    log.debug("DwrConnectionFactory.createConnection(username, password) is provided for convenience only. No passwords are used by DWR");
		return new DwrConnection();
	}

	/**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrConnectionFactory.class);
}
