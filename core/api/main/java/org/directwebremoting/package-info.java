
/**
This package contains all the classes that are interesting to DWR users going
about normal DWR business.

<p>It is entirely possible to use DWR without using any of these classes,
however as soon as you want to use advanced features like Reverse Ajax, you
will need to use {@link ScriptSession}s, and you get access so this and other
key classes using {@link WebContext}/{@link WebContextFactory} or
{@link ServerContext}/{@link ServerContextFactory}.</p>

<p>There are other packages that may be of use to average users:</p>
<ul>
<li>The JavaScript proxy classes are stored in org.directwebremoting.proxy.*</li>
<li>Various event classes are in org.directwebremoting.event.*</li>
<li>The {@link FileTransfer} is in org.directwebremoting.io<li>
</ul>

@author Joe Walker [joe at getahead dot ltd dot uk]
*/
package org.directwebremoting;

import org.directwebremoting.io.FileTransfer;
