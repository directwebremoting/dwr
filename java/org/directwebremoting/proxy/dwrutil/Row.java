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
package org.directwebremoting.proxy.dwrutil;

/**
 * A simple wrapper for a table row element.
 * This style of adding elements is very experimental.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Row extends HtmlElement
{
    /**
     * Make sure there is a default constructor
     */
    public Row()
    {
    }

    /**
     * Constructor to automatically create a set of cells
     * @param cellStrings String values for the cell contents
     */
    public Row(String[] cellStrings)
    {
        cells = new Cell[cellStrings.length];
        for (int i = 0; i < cellStrings.length; i++)
        {
            cells[i] = new Cell(cellStrings[i]);
        }
    }

    /**
     * Constructor to automatically create a set of cells
     * @param cells Cell contents
     */
    public Row(Cell[] cells)
    {
        this.cells = cells;
    }

    /**
     * @return the cells
     */
    public Cell[] getCells()
    {
        return cells;
    }

    /**
     * @param cells the cells to set
     */
    public void setCells(Cell[] cells)
    {
        this.cells = cells;
    }

    private Cell[] cells;
}
