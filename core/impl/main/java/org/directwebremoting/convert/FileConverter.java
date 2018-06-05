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
import org.directwebremoting.extend.ContainerUtil;
import org.directwebremoting.extend.DataUrlDownloadManager;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.InboundVariable;
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
                InputStreamFactory inFactory = new InputStreamFactory()
                {
                    public InputStream getInputStream() throws IOException
                    {
                        return formField.getInputStream();
                    }

                    public void close() throws IOException
                    {
                        formField.getInputStream().close();
                    }
                };
                return new FileTransfer(formField.getName(), formField.getMimeType(), formField.getFileSize(), inFactory);
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
    public OutboundVariable convertOutbound(final Object object, OutboundContext outboundContext) throws ConversionException
    {
        if (object == null)
        {
            return new NonNestedOutboundVariable("null");
        }

        try
        {
            FileTransfer transfer;

            if (object instanceof BufferedImage)
            {
                transfer = new FileTransfer((BufferedImage) object, "png");
            }
            else if (object instanceof InputStream)
            {
                final InputStream in = (InputStream) object;
                transfer = new FileTransfer("download.dat", "binary/octet-stream", -1, new InputStreamFactory()
                {
                    public InputStream getInputStream() throws IOException
                    {
                        return in;
                    }

                    public void close() throws IOException
                    {
                        in.close();
                    }
                });
            }
            else if (object instanceof FileTransfer)
            {
                transfer = (FileTransfer) object;
            }
            else if (object.getClass().isArray() && object.getClass().getComponentType() == Byte.TYPE)
            {
                transfer = new FileTransfer("download.dat", "binary/octet-stream", (byte[]) object);
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

            String url = downloadManager.addFileTransfer(transfer);
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
