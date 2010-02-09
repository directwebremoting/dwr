package org.directwebremoting.dwrp;

/**
 * An alarm is somthing that goes off at some point in the future.
 * An alarm is not primed until {@link #setAlarmAction(Sleeper)} is called.
 * {@link #setAlarmAction(Sleeper)} should only be called once before
 * {@link #cancel()} is called, and the latter should only be called once.
 * The alarm should not 'go off' after {@link #cancel()} has been called,
 * however since this is a multi-threadded environment, {@link Sleeper}s
 * should protect themselves from late calls to {@link Sleeper#wakeUp()}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
interface Alarm
{
    /**
     * Alarms need something to do when they go off.
     * After this method has been called the Alarm may 'go off', once only,
     * by calling {@link Sleeper#wakeUp()}.
     * @param sleeper The action to awake when the alarm goes off
     */
    void setAlarmAction(Sleeper sleeper);

    /**
     * Prevent further calls to {@link Sleeper#wakeUp()}.
     * See the note about late calls above.
     */
    void cancel();
}
