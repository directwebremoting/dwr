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
 * An interface to the various methods of compressing web resources.
 * This primarily means JavaScript, but could in theory extend to other
 * resources like CSS, HTML, etc.
 * @author David Marginian [david at butterdev dot com]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Compressor
{
    /**
     * Compress a JavaScript file to a smaller version of the original
     * @param script The script to compress
     * @return The compressed script
     * @throws Exception The implementations of this interface are all likely to
     * have different things they can throw. We are going to catch Exception
     * anyway because we can continue (by using uncompressed scripts) so why
     * force implementors to nest to another exception type when we can just let
     * them use the original exception?
     */
    public String compressJavaScript(String script) throws Exception;
}
