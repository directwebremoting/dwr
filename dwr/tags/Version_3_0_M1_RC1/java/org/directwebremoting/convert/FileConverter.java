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
package org.directwebremoting.convert;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.FileGenerator;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.impl.DataUrlDownloadManager;
import org.directwebremoting.impl.FileTransferFileGenerator;
import org.directwebremoting.impl.ImageIOFileGenerator;
import org.directwebremoting.impl.InputStreamFileGenerator;
import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.util.Messages;

/**
 * The FileConverter can only convert inbound files, convertOutbound is not
 * supported. 
 * Files come from an &lt;input type=&quot;file&quot;/&gt; on the client.
 * @author Lance Semmens [uklance at gmail dot com]
 */
public class FileConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data, InboundContext inctx) throws MarshallException
    {
        FormField formField = data.getFormField();
        if (paramType == FileTransfer.class)
        {
            return new FileTransfer(formField.getName(), formField.getMimeType(), formField.getInputStream());
        }
        else if (paramType == InputStream.class)
        {
            return formField.getInputStream();
        }
        else if (paramType == BufferedImage.class)
        {
            try
            {
                return ImageIO.read(formField.getInputStream());
            }
            catch (IOException ex)
            {
                throw new MarshallException(paramType, ex);
            }
        }

        throw new MarshallException(paramType, Messages.getString("MarshallException.FileFailure", paramType));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext outboundContext) throws MarshallException
    {
        if (object == null)
        {
            return new NonNestedOutboundVariable("null");
        }

        try
        {
            FileGenerator generator;

            if (object instanceof BufferedImage)
            {
                BufferedImage image = (BufferedImage) object;
                generator = new ImageIOFileGenerator(image, "image/png", "png");
            }
            else if (object instanceof InputStream)
            {
                InputStream in = (InputStream) object;
                generator = new InputStreamFileGenerator(in, "binary/octet-stream");
            }
            else if (object instanceof FileTransfer)
            {
                FileTransfer in = (FileTransfer) object;
                generator = new FileTransferFileGenerator(in);
            }
            else
            {
                throw new MarshallException(object.getClass());
            }

            DownloadManager downloadManager;
            if (preferDataUrlSchema && isDataUrlAvailable())
            {
                downloadManager = new DataUrlDownloadManager();
            }
            else
            {
                downloadManager = WebContextFactory.get().getContainer().getBean(DownloadManager.class);
            }

            String url = downloadManager.addFile(generator);
            return new NonNestedOutboundVariable(url);
        }
        catch (IOException ex)
        {
            throw new MarshallException(getClass(), ex);
        }
    }

    /**
     * Is the data: URL allowed by the current browser.
     * @return true if data: is allowed
     */
    protected boolean isDataUrlAvailable()
    {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        return request.getHeader("user-agent").indexOf("MSIE") == -1;
    }

    /**
     * Do we use a data: URL when we know it will work
     * @param preferDataUrlSchema
     */
    public void setPreferDataUrlSchema(boolean preferDataUrlSchema)
    {
        this.preferDataUrlSchema = preferDataUrlSchema;
    }

    /**
     * Do we use data: URLs when we can?
     */
    private boolean preferDataUrlSchema = false;
}
