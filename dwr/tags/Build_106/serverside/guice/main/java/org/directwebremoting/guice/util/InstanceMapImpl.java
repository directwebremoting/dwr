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

import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Key;

/**
 * Implementation of {@link InstanceMap} in terms of {@code ConcurrentHashMap}.
 * @author Tim Peierls [tim at peierls dot net]
 */
class InstanceMapImpl<T> extends ConcurrentHashMap<Key<T>, InstanceProvider<T>> implements InstanceMap<T>
{
}
