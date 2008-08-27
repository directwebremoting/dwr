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


/**
 * Parameters used to configure DWR.
 * @author Tim Peierls [tim at peierls dot net]
 */
public enum ParamName
{
    ALLOW_GET_FOR_SAFARI                ("allowGetForSafariButMakeForgeryEasier"),
    CROSS_DOMAIN_SESSION_SECURITY       ("crossDomainSessionSecurity"),
    ALLOW_SCRIPT_TAG_REMOTING           ("allowScriptTagRemoting"),
    DEBUG                               ("debug"),
    SCRIPT_SESSION_TIMEOUT              ("scriptSessionTimeout"),
    MAX_CALL_COUNT                      ("maxCallCount"),
    ACTIVE_REVERSE_AJAX_ENABLED         ("activeReverseAjaxEnabled"),
    MAX_WAIT_AFTER_WRITE                ("maxWaitAfterWrite"),
    DISCONNECTED_TIME                   ("disconnectedTime"),
    POLL_AND_COMET_ENABLED              ("pollAndCometEnabled"),
    MAX_WAITING_THREADS                 ("maxWaitingThreads"),
    MAX_POLL_HITS_PER_SECOND            ("maxPollHitsPerSecond"),
    PRE_STREAM_WAIT_TIME                ("preStreamWaitTime"),
    POST_STREAM_WAIT_TIME               ("postStreamWaitTime"),
    IGNORE_LAST_MODIFIED                ("ignoreLastModified"),
    SESSION_COOKIE_NAME                 ("sessionCookieName"),
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
