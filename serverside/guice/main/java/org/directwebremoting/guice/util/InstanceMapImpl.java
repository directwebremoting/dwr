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
