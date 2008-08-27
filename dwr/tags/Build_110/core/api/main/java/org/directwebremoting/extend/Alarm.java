package org.directwebremoting.extend;

/**
 * An alarm is something that goes off at some point in the future.
 * Alarms must be constructed with a {@link Sleeper} so they can act to tell
 * the Sleeper to awake (or not to sleep) at any time until {@link #cancel()}
 * is called.
 * The alarm should not 'go off' after {@link #cancel()} has been called,
 * however, {@link Sleeper}s should still protect themselves from late calls to
 * {@link Sleeper#wakeUp()}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Alarm
{
    /**
     * Prevent further calls to {@link Sleeper#wakeUp()}.
     * See the note about late calls above.
     */
    void cancel();
}
