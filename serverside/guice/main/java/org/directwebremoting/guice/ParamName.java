package org.directwebremoting.guice;


/**
 * Parameters used to configure DWR.
 * @author Tim Peierls [tim at peierls dot net]
 */
public enum ParamName
{
    ALLOW_GET                           ("allowGetButMakeForgeryEasier"),
    CROSS_DOMAIN_SESSION_SECURITY       ("crossDomainSessionSecurity"),
    ALLOW_SCRIPT_TAG_REMOTING           ("allowScriptTagRemoting"),
    DEBUG                               ("debug"),
    SCRIPT_SESSION_TIMEOUT              ("scriptSessionTimeout"),
    MAX_CALL_COUNT                      ("maxCallCount"),
    ACTIVE_REVERSE_AJAX_ENABLED         ("activeReverseAjaxEnabled"),
    MAX_WAIT_AFTER_WRITE                ("maxWaitAfterWrite"),
    STREAMING_ENABLED                   ("streamingEnabled"),
    DISCONNECTED_TIME                   ("disconnectedTime"),
    POLL_AND_COMET_ENABLED              ("pollAndCometEnabled"),
    MAX_WAITING_THREADS                 ("maxWaitingThreads"),
    MAX_POLL_HITS_PER_SECOND            ("maxPollHitsPerSecond"),
    PRE_STREAM_WAIT_TIME                ("preStreamWaitTime"),
    POST_STREAM_WAIT_TIME               ("postStreamWaitTime"),
    IGNORE_LAST_MODIFIED                ("ignoreLastModified"),
    WELCOME_FILES                       ("welcomeFiles"),
    NORMALIZE_INCLUDES_QUERY_STRING     ("normalizeIncludesQueryString"),
    OVERRIDE_PATH                       ("overridePath"),
    CLASSES                             ("classes");

    ParamName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    private final String name;
}
