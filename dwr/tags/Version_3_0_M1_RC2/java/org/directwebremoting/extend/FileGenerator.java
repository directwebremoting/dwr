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

import java.io.IOException;
import java.io.OutputStream;

/**
 * A representation of the file that we are writing out.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface FileGenerator
{
    /**
     * Write the file to the give {@link OutputStream}.
     * The system will take care of opening and closing the stream
     * @param out The stream to write to.
     * @throws IOException If there are creation problems
     */
    void generateFile(OutputStream out) throws IOException;

    /**
     * What is the MimeType for the given file
     * @return a valid mime-type
     */
    String getMimeType();
}
