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
package org.directwebremoting.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class wraps the getting of an input stream in a closure to avoid
 * issues with not closing the stream. An input stream will only be retrieved
 * when it is needed
 * @author Lance Semmens [uklance at gmail dot com]
 */
public interface InputStreamFactory
{
    /**
     * Gets an input stream
     * @return the input stream
     */
    public InputStream getInputStream() throws IOException;
}
