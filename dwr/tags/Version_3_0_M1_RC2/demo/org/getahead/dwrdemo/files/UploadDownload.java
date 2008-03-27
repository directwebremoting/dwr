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
package org.getahead.dwrdemo.files;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import org.directwebremoting.io.FileTransfer;
import org.getahead.dwrdemo.util.ColorUtil;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * A demonstration of uploading files and images
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class UploadDownload
{
    /**
     * Take 2 uploaded files and return an image based on them
     * @param uploadImage The uploaded image
     * @param uploadFile The uploaded file
     * @param color The selected color
     * @return A mangled image based on the 2 uploaded files
     */
    public BufferedImage uploadFiles(BufferedImage uploadImage, String uploadFile, String color)
    {
        uploadImage = scaleToSize(uploadImage);
        uploadImage = grafitiTextOnImage(uploadImage, uploadFile, color);

        return uploadImage;
    }

    /**
     * Generates a PDF file with the given text
     * http://itext.ugent.be/itext-in-action/
     * @return A PDF file as a byte array
     */
    public FileTransfer downloadPdfFile(String contents) throws Exception
    {
        if (contents == null || contents.length() == 0)
        {
            contents = "[BLANK]";
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, buffer);

        document.addCreator("DWR.war using iText");
        document.open();
        document.add(new Paragraph(contents));
        document.close();

        byte[] data = buffer.toByteArray();
        return new FileTransfer("ExamplePDF", "application/pdf", data);
    }

    /**
     * Voodoo to scale the image to 200x200
     * @param uploadImage The image to work on
     * @return The altered image
     */
    private BufferedImage scaleToSize(BufferedImage uploadImage)
    {
        AffineTransform atx = new AffineTransform();
        atx.scale(200d / uploadImage.getWidth(), 200d / uploadImage.getHeight());
        // AffineTransformOp.TYPE_BILINEAR is very slow
        AffineTransformOp afop = new AffineTransformOp(atx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        uploadImage = afop.filter(uploadImage, null);
        return uploadImage;
    }

    /**
     * And scrawl the text on the image in 10 rows of 21 chars
     * @param uploadImage The image to work on
     * @param uploadFile The text to write on the image
     * @param color The selected color
     * @return The altered image
     */
    private BufferedImage grafitiTextOnImage(BufferedImage uploadImage, String uploadFile, String color)
    {
        StringBuilder buffer = new StringBuilder();
        while (buffer.length() < 200)
        {
            buffer.append(" ");
            buffer.append(uploadFile);
        }

        Graphics2D g2d = uploadImage.createGraphics();
        for (int row = 0; row < 10; row++)
        {
            String output = null;
            if (buffer.length() > (row + 1) * CHARS_PER_LINE)
            {
                output = buffer.substring(row * CHARS_PER_LINE, (row + 1) * CHARS_PER_LINE);
            }
            else
            {
                output = buffer.substring(row * CHARS_PER_LINE);
            }

            g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2d.setColor(ColorUtil.decodeHtmlColorString(color));
            g2d.drawString(output, 5, (row + 1) * CHARS_PER_LINE);
        }

        return uploadImage;
    }

    /**
     * 
     */
    private static final int CHARS_PER_LINE = 21;
}
