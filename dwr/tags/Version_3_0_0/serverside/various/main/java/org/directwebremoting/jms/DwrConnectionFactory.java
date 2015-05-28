/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
