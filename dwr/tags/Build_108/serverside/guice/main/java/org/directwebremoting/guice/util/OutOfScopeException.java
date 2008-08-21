/*
 * Copyright 2008 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice.util;

import com.google.inject.Key;
import com.google.inject.Scope;

/**
 * Thrown by Providers returned by
 * {@link com.google.inject.Scope#scope scope(Key, Provider)}
 * when they cannot locate a resource needed to resolve a key.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class OutOfScopeException extends RuntimeException
{
    public OutOfScopeException(Scope scope, Key<?> key, Throwable cause)
    {
        super(String.format(
            "Not in scope %s for key %s: caused by %s",
            scope, key, cause
        ), cause);
    }
}
