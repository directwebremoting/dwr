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
package org.directwebremoting.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * A way to convert {@link BufferedImage}s to files so they can be written
 * using a FileServingServlet or similar.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ImageIOFileGenerator extends AbstractFileGenerator
{
    /**
     * Setup the image to convert
     * @param image the image to convert
     * @param mimeType The mime type to convert the image into
     * @param type {@link ImageIO} type
     */
    public ImageIOFileGenerator(BufferedImage image, String mimeType, String basename, String type)
    {
        super(basename + "." + type, mimeType);
        this.image = image;
        this.type = type;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.DownloadManager.FileGenerator#generateFile(java.io.OutputStream)
     */
    public void generateFile(OutputStream out) throws IOException
    {
        ImageIO.write(image, type, out);
    }

    /**
     * The extension for the filename to go with the mime-type
     */
    protected String type;

    /**
     * The image that we are about to export
     */
    protected final BufferedImage image;
}
