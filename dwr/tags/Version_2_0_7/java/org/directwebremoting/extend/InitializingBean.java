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
package org.directwebremoting.extend;

import org.directwebremoting.Container;

/**
 * Interface to be implemented by beans that need to react once all their
 * properties have been set by a {@link Container}: for example, to perform
 * custom initialization.
 * <p>This is similar in concept to the Spring
 * {@link org.springframework.beans.factory.InitializingBean}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface InitializingBean
{
    /**
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set
     * @param container The container that is doing the initialization
     */
    void afterContainerSetup(Container container);
}
