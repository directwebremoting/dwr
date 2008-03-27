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
package org.directwebremoting.drapgen.loader;

import org.directwebremoting.drapgen.ast.Project;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Loader
{
    /**
     * Convert the data understood by this {@link Loader} into an existing
     * {@link Project}. This should not affect the data existing in the
     * Project.
     */
    void loadToProject(Project project);
}
