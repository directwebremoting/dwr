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
package org.getahead.dwrdemo.images;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.getahead.dwrdemo.util.ColorUtil;

/**
 * Demonstrate DWR's {@link org.directwebremoting.convert.FileConverter}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ImageGenerator
{
    /**
     * 
     */
    public void addQuote(String text, String fontName, boolean bold, boolean italic, int size, String color)
    {
        while (quotes.size() > 50)
        {
            quotes.remove(0);
        }

        Quote quote = new Quote();

        quote.color = ColorUtil.decodeHtmlColorString(color);
        int style = (bold ? Font.BOLD : 0) + (italic ? Font.ITALIC : 0);
        quote.font = new Font(fontName, style, size);
        quote.text = text;
        quote.x = random.nextInt(200);
        quote.y = random.nextInt(400);

        quotes.add(quote);

        update();
    }

    /**
     * Generate an image to demonstrate DWR's
     * {@link org.directwebremoting.convert.FileConverter}
     */
    private void update()
    {
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Quote quote : quotes)
        {
            g2d.setColor(quote.color);
            g2d.setFont(quote.font);
            g2d.drawString(quote.text, quote.x, quote.y);
        }

        g2d.dispose();

        WebContext webContext = WebContextFactory.get();
        String contextPath = webContext.getContextPath();
        Collection<ScriptSession> sessions = webContext.getScriptSessionsByPage(contextPath + "/images/");
        Util util = new Util(sessions);
        util.setValue("image", bufferedImage);
    }

    class Quote
    {
        Font font;
        Color color;
        int x;
        int y;
        String text;
    }

    private static final Random random = new Random();

    private final List<Quote> quotes = new ArrayList<Quote>();
}
