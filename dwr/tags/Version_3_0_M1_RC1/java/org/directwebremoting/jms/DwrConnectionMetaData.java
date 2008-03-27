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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;

import org.directwebremoting.util.VersionUtil;

/**
 * An implementation of {@link ConnectionMetaData} that uses DWR meta-data.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrConnectionMetaData implements ConnectionMetaData
{
	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getJMSMajorVersion()
	 */
	public int getJMSMajorVersion() throws JMSException
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getJMSMinorVersion()
	 */
	public int getJMSMinorVersion() throws JMSException
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getJMSProviderName()
	 */
	public String getJMSProviderName() throws JMSException
	{
		return "DWR";
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getJMSVersion()
	 */
	public String getJMSVersion() throws JMSException
	{
		return "1.1";
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getJMSXPropertyNames()
	 */
	public Enumeration<String> getJMSXPropertyNames() throws JMSException
	{
		return Collections.enumeration(properties);
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getProviderMajorVersion()
	 */
	public int getProviderMajorVersion() throws JMSException
	{
        String version = getProviderVersion();
        String[] strings = version.split("\\.");
        return Integer.parseInt(strings[0]);
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getProviderMinorVersion()
	 */
	public int getProviderMinorVersion() throws JMSException
	{
        String version = getProviderVersion();
        String[] strings = version.split("\\.");
        return Integer.parseInt(strings[1]);
	}

	/* (non-Javadoc)
	 * @see javax.jms.ConnectionMetaData#getProviderVersion()
	 */
	public String getProviderVersion() throws JMSException
	{
		return VersionUtil.getVersion();
	}

	private Collection<String> properties = new ArrayList<String>();
}
