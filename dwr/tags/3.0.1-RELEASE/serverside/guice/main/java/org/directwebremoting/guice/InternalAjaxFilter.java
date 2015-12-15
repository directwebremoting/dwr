/*
 * Copyright 2007 Tim Peierls
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
package org.directwebremoting.guice;

import java.lang.reflect.Method;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

import com.google.inject.Provider;

/**
 * Specialized Ajax filter implementation that uses a Provider to
 * look up instances to delegate to. This class is used by
 * {@link InternalAjaxFilterManager}.
 * @author Tim Peierls [tim at peierls dot net]
 */
class InternalAjaxFilter implements AjaxFilter
{
    InternalAjaxFilter(Provider<AjaxFilter> provider)
    {
        this.provider = provider;
    }

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain)
        throws Exception
    {
        return provider.get().doFilter(obj, method, params, chain);
    }

    private final Provider<AjaxFilter> provider;
}
