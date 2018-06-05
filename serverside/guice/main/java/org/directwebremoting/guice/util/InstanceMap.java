package org.directwebremoting.guice.util;

import java.util.concurrent.ConcurrentMap;

import com.google.inject.Key;

/**
 * A specialization of ConcurrentMap with keys of type {@code Key<T>} and
 * values of type {@code InstanceProvider<T>}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public interface InstanceMap<T> extends ConcurrentMap<Key<T>, InstanceProvider<T>>
{
}
