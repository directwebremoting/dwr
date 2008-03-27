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
package org.directwebremoting.drapgen.ast;

import static org.directwebremoting.drapgen.ast.SerializationStrings.*;

/**
 * We are assuming that there is no point to non-public constructors for Drapgen
 * @see java.lang.reflect.Constructor
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Constructor extends Subroutine
{
    /**
     * All {@link Constructor}s need a parent {@link Type}
     * @param parent the type of which we are a part
     */
    protected Constructor(Type parent)
    {
        super(parent);
    }

    /**
     * Create a XOM Element from this
     * @return a Element representing this Type
     */
    protected nu.xom.Element toXomElement()
    {
        return super.toXomElement(CONSTRUCTOR);
    }
}
