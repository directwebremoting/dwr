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
package org.directwebremoting.extend;

/**
 * An interface to resolve the fact that many webservers treat blah/index.html
 * the same as blah/.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface PageNormalizer
{
    /**
     * Take an un-normalized URL and turn it into the canonical form for that
     * URL. In general this will work by stripping off extra components like
     * <code>index.html</code> rather than adding them in.
     * @param unnormalized The raw string from the browser
     * @return A canonical form that DWR uses to compare pages for equivalence.
     */
    public String normalizePage(String unnormalized);
}
