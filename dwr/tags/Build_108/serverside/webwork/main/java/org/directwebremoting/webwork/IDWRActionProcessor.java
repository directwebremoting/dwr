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
package org.directwebremoting.webwork;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <code>DWRAction</code> pre/post processor.
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public interface IDWRActionProcessor
{
    /**
     *
     * @param request
     * @param originalResponse
     * @param actionResponse
     * @param invocationParameters
     */
    void preProcess(HttpServletRequest request, HttpServletResponse originalResponse, HttpServletResponse actionResponse, Map<String, String> invocationParameters);

    /**
     *
     * @param request
     * @param originalResponse
     * @param actionResponse
     * @param result
     */
    void postProcess(HttpServletRequest request, HttpServletResponse originalResponse, HttpServletResponse actionResponse, AjaxResult result);
}

