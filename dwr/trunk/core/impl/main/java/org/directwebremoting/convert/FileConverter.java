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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.Container;
import org.directwebremoting.ConversionException;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.ByteArrayFileGenerator;
import org.directwebremoting.extend.ContainerUtil;
import org.directwebremoting.extend.DataUrlDownloadManager;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.FileGenerator;
import org.directwebremoting.extend.FileTransferFileGenerator;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ImageIOFileGenerator;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.InputStreamFileGenerator;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.io.InputStreamFactory;
import org.directwebremoting.util.BrowserDetect;
import org.directwebremoting.util.CopyUtils;
import org.directwebremoting.util.UserAgent;

/**
 * The FileConverter can only convert inbound files, convertOutbound is not
 * supported.
 * Files come from an &lt;input type=&quot;file&quot;/&gt; on the client.
 * @author Lance Semmens [uklance at gmail dot com]
 * @author Joe Walker [joe at getahead dot org]
 * @author Niklas Johansson [niklas dot json at gmail dot com]
 */
public class FileConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        try
        {
            final FormField formField = data.getFormField();
            if (paramType == FileTransfer.class)
            {
                return new FileTransfer(formField.getName(), formField.getMimeType(), formField.getFileSize(), new InputStreamFactory() {
                    public InputStream getInputStream() throws IOException {
                        return formField.getInputStream();
                    }
                });
            }
            else if (paramType == InputStream.class)
            {
                return formField.getInputStream();
            }
            else if (paramType == BufferedImage.class)
            {
                return ImageIO.read(formField.getInputStream());
            }
            else if (paramType.isArray() && paramType.getComponentType() == Byte.TYPE)
            {
                InputStream in = formField.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                CopyUtils.copy(in, out);
                return out.toByteArray();
            }
        }
        catch (IOException ex)
        {
            throw new ConversionException(paramType, ex);
        }

        throw new ConversionException(paramType, "File conversion is not possible for a " + paramType);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext outboundContext) throws ConversionException
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
                generator = new ImageIOFileGenerator(image, "image/png", "image", "png");
            }
            else if (object instanceof InputStream)
            {
                InputStream in = (InputStream) object;
                generator = new InputStreamFileGenerator(in, "download.dat", "binary/octet-stream");
            }
            else if (object instanceof FileTransfer)
            {
                FileTransfer in = (FileTransfer) object;
                generator = new FileTransferFileGenerator(in);
            }
            else if (object.getClass().isArray() && object.getClass().getComponentType() == Byte.TYPE)
            {
                byte[] buffer = (byte[]) object;
                generator = new ByteArrayFileGenerator(buffer, "download.dat", "binary/octet-stream");
            }
            else
            {
                throw new ConversionException(object.getClass());
            }

            Container container = WebContextFactory.get().getContainer();
            boolean preferDataUrlSchema = ContainerUtil.getBooleanSetting(container, "preferDataUrlSchema", false);

            DownloadManager downloadManager;
            if (preferDataUrlSchema && isDataUrlAvailable())
            {
                downloadManager = new DataUrlDownloadManager();
            }
            else
            {
                downloadManager = container.getBean(DownloadManager.class);
            }

            String url = downloadManager.addFile(generator);
            return new NonNestedOutboundVariable(url);
        }
        catch (IOException ex)
        {
            throw new ConversionException(getClass(), ex);
        }
    }

    /**
     * Is the data: URL allowed by the current browser.
     * @return true if data: is allowed
     */
    protected boolean isDataUrlAvailable()
    {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();

        return BrowserDetect.atLeast(request, UserAgent.IE, 8) ||
               BrowserDetect.atLeast(request, UserAgent.Gecko, 20041107) ||
               BrowserDetect.atLeast(request, UserAgent.AppleWebKit, 2) ||
               BrowserDetect.atLeast(request, UserAgent.Opera, 8);
    }
}
