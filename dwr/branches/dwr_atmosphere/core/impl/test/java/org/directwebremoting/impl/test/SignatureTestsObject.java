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
package org.directwebremoting.impl.test;

import java.util.List;

/**
 * @author Bram Smeets
 */
public class SignatureTestsObject
{
    /**
     * @return the nos
     */
    public List<Integer> getLotteryResults()
    {
        return nos;
    }

    /**
     * @param nos
     */
    public void setLotteryResults(List<Integer> nos)
    {
        this.nos = nos;
        // do nothing
    }

    private List<Integer> nos;
}
