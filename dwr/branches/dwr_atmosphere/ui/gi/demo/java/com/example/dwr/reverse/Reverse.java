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
package com.example.dwr.reverse;

import java.util.Date;

import jsx3.GI;
import jsx3.app.Model;
import jsx3.app.Server;
import jsx3.gui.Block;
import jsx3.gui.Button;
import jsx3.gui.ColorPicker;
import jsx3.gui.DatePicker;
import jsx3.gui.Form;
import jsx3.gui.Select;
import jsx3.gui.Slider;
import jsx3.gui.TextBox;

import org.directwebremoting.Browser;
import org.directwebremoting.ServerContextFactory;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Reverse
{
    public void buttonEnable()
    {
        getServer().getJSX("button", Button.class).setEnabled(Form.STATEENABLED, true);
    }

    public void buttonDisable()
    {
        getServer().getJSX("button", Button.class).setEnabled(Form.STATEDISABLED, true);
    }

    public void select(String index)
    {
        getServer().getJSX("select", Select.class).setValue(index);
    }

    public void slide(int position)
    {
        getServer().getJSX("slider", Slider.class).setValue(position);
    }

    public void text(String message)
    {
        getServer().getJSX("textbox", TextBox.class).setValue(message);
    }

    public void textEnable()
    {
        getServer().getJSX("textbox", TextBox.class).setEnabled(Form.STATEENABLED, true);
    }

    public void textDisable()
    {
        getServer().getJSX("textbox", TextBox.class).setEnabled(Form.STATEDISABLED, true);
    }

    public void dateToday()
    {
        getServer().getJSX("datePicker", DatePicker.class).setDate(new Date());
    }

    public void dateEpoch()
    {
        getServer().getJSX("datePicker", DatePicker.class).setDate(new Date(0));
    }

    public void color(String color)
    {
        getServer().getJSX("colorPicker", ColorPicker.class).setValue(color);
    }

    public void create()
    {
        Button button = new Button("createCreated", 0, 0, 300, "Created Button");
        getServer().getJSX("blockCreate", Block.class).setChild(button, Model.PERSISTNONE, (String) null, null);
    }

    /**
     *
     */
    private Server getServer()
    {
        final Server[] reply = new Server[1];
        String page = ServerContextFactory.get().getContextPath() + "/gi/reverse.html";
        Browser.withPage(page, new Runnable()
        {
            public void run()
            {
                reply[0] = GI.getServer("reverse");
            }
        });

        return reply[0];
    }
}
