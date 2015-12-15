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
package com.example.dwr.gidemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A manager for a set of corporations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Corporations
{
    /**
     * Create a set of random corporations
     */
    public Corporations()
    {
        corporations.add(new Corporation("BAYC", "Bay Corporation"));
        corporations.add(new Corporation("VIRT", "Virtualis"));
        corporations.add(new Corporation("MGSF", "Megasoft Co."));
        corporations.add(new Corporation("MAXI", "Maximegalon Ind."));
        corporations.add(new Corporation("RIGL", "Rigel Cybernetics"));
        corporations.add(new Corporation("TRTR", "Tretrigintillion"));
        corporations.add(new Corporation("CONC", "Concor PLC"));
        corporations.add(new Corporation("HYPS", "Hypersoft"));
    }

    /**
     *
     */
    public List<Corporation> getCorporations()
    {
        return corporations;
    }

    /**
     *
     */
    public Corporation getNextChangedCorporation()
    {
        // Who's is gonna be
        Corporation corporation = corporations.get(random.nextInt(corporations.size()));
        corporation.change();

        return corporation;
    }

    /**
     * Used to generate random data
     */
    private Random random = new Random();

    /**
     * The corporations that we manage
     */
    private List<Corporation> corporations = new ArrayList<Corporation>();
}
