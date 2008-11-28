/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.spring;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

/**
 * Automatically maps (at context startup) the URLs needed by DWR to a DWRController. 
 * 
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class DwrHandlerMapping extends SimpleUrlHandlerMapping {

	private static final Log log = LogFactory.getLog(DwrHandlerMapping.class);

	/**
	 * Maps the URLs (/engine.js, /util.js, /interface/**, /call/**) and continues.
	 */
	@Override
    public void initApplicationContext() throws BeansException {
	    String[] dwrControllerName = getApplicationContext().getBeanNamesForType(DwrController.class);
	    Assert.notEmpty(dwrControllerName, "No DWR Controller bean definition found.");
		Object handler = getApplicationContext().getBean(dwrControllerName[0]);
		Map<String, Object> mappings = new HashMap<String, Object>();
		mappings.put("/engine.js", handler);
		mappings.put("/util.js", handler);
		mappings.put("/interface/**", handler);
		mappings.put("/call/**", handler);
		mappings.put("/test/**", handler);
                mappings.put("/download/**", handler);
		if (log.isDebugEnabled())
		{
			log.info("[engine.js] mapped to dwrController");
			log.info("[util.js] mapped to dwrController");
			log.info("Interface beans and calls (/interface/*, /call/*) mapped to dwrController");
			log.info("/test/* has been mapped to dwrController");
			log.info("/download/* has been mapped to dwrController");
		}
		setUrlMap(mappings);
		super.initApplicationContext();
	}

}
