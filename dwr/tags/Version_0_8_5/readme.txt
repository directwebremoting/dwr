
See www/index.html for more information.


About ANT tasks
---------------
cvsup syncs with CVS
war compiles into dwr/web/WEB-INF/classes in a similar way to eclipse
targetwar creates a war directory in dwr/target/ant/war for external deployment
clean undoes the previous 2.


About role membership
---------------------
I approve anyone to be an observer, but generally deny requests for developer
access preferring more controlled access to CVS. My standard reply is:
Hi,
I have generaly worked on open source projects using a patch system rather than very open CVS which often ends up with much wasted time spent on resolving developer conflicts, and I'd like to use that model on this project too.
Thanks,
Joe.


Hype
----
If I post an announcement to some site I record it here, so I can cut and paste next time faster

Remember TSS does not support https URLs

Java.net: 2005-05-30
New features in DWR 0.8
http://www.getahead.ltd.uk/dwr/
http://www.getahead.ltd.uk/blog/joe/2005/05/30/1117455081304.html
<a href="http://www.getahead.ltd.uk/dwr/">DWR</a> version 0.8 contains a big list of new features. You can <a href="https://dwr.dev.java.net/servlets/ProjectDocumentList?folderID=3450&expandFolder=3450&folderID=0">download version 0.8 from java.net</a>. The change log includes: batch sending of remote calls, fine grained access control, J2EE role based authorization, and enhanced debug pages.
We are working on a smaller list for the next version so we are not that far from 1.0.

Java.net: 2005-05-19
DWR 0.6 Released. AJAX and XMLHttpRequest made easy
http://www.getahead.ltd.uk/dwr/
http://www.getahead.ltd.uk/blog/joe/2005/05/19/1116499666404.html
Joe Walker has just released <a href="http://www.getahead.ltd.uk/dwr/">DWR version 0.6</a>. This release contains lots of new features, including support for automatic marshalling of DOM trees, better Spring support, better browser support and optional commons-logging support.
The full change list includes:
- New ScriptedCreator so you can use Groovy (or any other language supported by BSF), etc to configure new interface objects.
- Wider support for browsers. DWR now works with Firefox, Safari, IE, Konqueror, Opera, and Mozilla.
- The SpringCreator will now read from bean files configured in web.xml
- Logging via commons-logging if it is avaiable, or the old Servlet.log method if not.
- New converters for DOM, JDOM, XOM and DOM4J so you can pass DOM trees between the client and server without thinking
- Fix to support Jetty
- URL tweak to provide more info to web log analysers
- New ContextCreator to allow remoted objects to be stored in the servlet application context
- 2 New utilities in DWRUtil: Range selection, and return event handling
- There is also better documentation, error messages that are easier to decipher, better performance and other bug fixes
You can download the <a href="https://dwr.dev.java.net/servlets/ProjectDocumentList?folderID=3398">jar file and sources from java.net</a>. Or see the <a href="http://www.getahead.ltd.uk/dwr/">main project page.

TSS: 2005-05-19
DWR 0.6 Released. AJAX and XMLHttpRequest made easy
Joe Walker has just released <a href="http://www.getahead.ltd.uk/dwr/">DWR version 0.6</a>. This release contains lots of new features, including support for automatic marshalling of DOM trees, better Spring support, better browser support and optional commons-logging support.
The full change list includes:
- New ScriptedCreator so you can use Groovy (or any other language supported by BSF), etc to configure new interface objects.
- Wider support for browsers. DWR now works with Firefox, Safari, IE, Konqueror, Opera, and Mozilla.
- The SpringCreator will now read from bean files configured in web.xml
- Logging via commons-logging if it is avaiable, or the old Servlet.log method if not.
- New converters for DOM, JDOM, XOM and DOM4J so you can pass DOM trees between the client and server without thinking
- Fix to support Jetty
- URL tweak to provide more info to web log analysers
- New ContextCreator to allow remoted objects to be stored in the servlet application context
- 2 New utilities in DWRUtil: Range selection, and return event handling
- There is also better documentation, error messages that are easier to decipher, better performance and other bug fixes
You can download the <a href="https://dwr.dev.java.net/servlets/ProjectDocumentList?folderID=3398">jar file and sources from java.net</a>. Or see the <a href="http://www.getahead.ltd.uk/dwr/">main project page</a>.

Java.net: 2005-04-30
DWR 0.5 Released: Ajax and XMLHttpRequest made simple
http://www.getahead.ltd.uk/dwr/
http://www.getahead.ltd.uk/blog/joe/2005/04/28/1114692652085.html
Joe Walker has released <a href="http://www.getahead.ltd.uk/dwr/">DWR</a> (Direct Web Remoting) version 0.5. DWR makes AJAX easy by allowing you to call Java code directly from within browser JavaScript.
New in version 0.5 is improved integration with Spring, better Javascript libraries to cut down on the mucking about with DHTML and a new website with <a href="http://www.getahead.ltd.uk/dwr/demo-text.html">worked examples</a>.

TSS: 2005-04-30
DWR 0.5 Released: Ajax and XMLHttpRequest made simple
Joe Walker has released DWR (Direct Web Remoting) version 0.5. DWR makes AJAX easy by allowing you to call Java code directly from within browser JavaScript.
New in version 0.5 is improved integration with Spring, better Javascript libraries to cut down on the mucking about with DHTML and a new website with worked examples.
DWR is more full-featured and easier to use that alternatives like JSON-RPC. And that's not just the author's opinion, it must be true because Matt Riable says so.

TSS: 2005-03-07
Direct Web Remoting: Call server-side Java from JavaScript
<a href="http://dwr.dev.java.net/">Direct Web Remoting</a> (DWR) is a very simple way to call Java code directly from within browser JavaScript. New in version 0.4 is the ability to convert Javascript arrays into Java arrays, Java Objects into Javascript objects and so on.
DWR gets rid of almost all the boiler plate code between the web browser and your Java code. So you don't need to create servlets, web.xml, struts config files or JSF magic incantations, no writing Actions or implementing some special interface.
It helps you create sites like G-Mail or Google Suggest that update themselves without loading new pages.
<a href="http://dwr.dev.java.net/">DWR</a> comes as a simple jar file and a few lines to add to your web.xml file to configure the remoted classes. In a web page you add a couple of &lt;script&gt; tags to indicate which classes you wish to import and can then call your Java code directly from Javascript. Instructions and examples are available on the DWR site.

TSS: 2004-12-11
Direct Web Remoting: Call server-side Java from JavaScript
<a href="http://dwr.dev.java.net/">Direct Web Remoting</a> (DWR) is a very simple way to call Java code directly from within browser JavaScript. It helps you create sites like G-Mail, Google Suggest or Kuro5hin that update themselves without loading new pages.
<a href="http://dwr.dev.java.net/">DWR</a> comes as a simple jar file and a few lines to add to your web.xml file to configure the remoted classes. In a web page you add a couple of &lt;script&gt; tags to indicate which classes you wish to import and can then call your Java code directly from Javascript. More detailed instructions are available on the DWR site.
The Javascript works by dynamically creating an iframe through which it calls the DWR servlet with instructions about what Java code to call. The DWR servlet marshals the parameters, calls the server-side Java code and returns the reply to the iframe, which triggers an onload, which returns the reply to the calling Javascript.

CafeAuLait: 2004-12-15
I thought you might be interested in DWR for Cafe-Au-Lait. Version 0.3 just released:
http://dwr.dev.java.net/
DWR (Direct Web Remoting) is a very simple way to call server-side Java code directly from browser JavaScript. It helps you create sites like G-Mail, Google Suggest or Kuro5hin that update themselves without loading new pages.
DWR comes as a simple jar file and a few lines to add to your web.xml file to configure the remoted classes. In a web page you add a couple of &lt;script&gt; tags to indicate which classes you wish to import and can then call your Java code directly from Javascript.
The Javascript works, depending on your browser, either using XMLHttpRequest or by dynamically creating an iframe. Through this it calls the DWR servlet with instructions about what Java code to call. The DWR servlet marshals the parameters, calls the server-side Java code and returns the reply, which returns the reply to the calling Javascript.
More detailed instructions are available on the DWR site.

Java.Net: 2004-12-15
DWR 0.3 Released: Call server side Java directly from browser Javascript  
http://dwr.dev.java.net/
DWR (Direct Web Remoting) is a very simple way to call server-side Java code directly from browser JavaScript. It helps you create sites like G-Mail, Google Suggest or Kuro5hin that update themselves without loading new pages.
DWR comes as a simple jar file and a few lines to add to your web.xml file to configure the remoted classes. In a web page you add a couple of &lt;script&gt; tags to indicate which classes you wish to import and can then call your Java code directly from Javascript.
The Javascript works, depending on your browser, either using XMLHttpRequest or by dynamically creating an iframe. Through this it calls the DWR servlet with instructions about what Java code to call. The DWR servlet marshals the parameters, calls the server-side Java code and returns the reply, which returns the reply to the calling Javascript.
More detailed instructions are available on the DWR site: http://dwr.dev.java.net/

                 traffic rank  reach/mil
developer.com        11,000      120
sys-con.com          12,000      110 (java dev journal)
javaworld.com        13,000       65
theserverside.com    15,000       90
javalobby.org        17,000       60
onjava.com           25,000       70
java.net             24,000       70
raibledesigns.com    50,000       28
fawcette.com         57,000       25 (java pro)
cafeaulait.org       86,000       16
javablogs.com       114,000       12
javacoffeebreak.com 115,000       12
getahead.ltd.uk     315,000        8


License
-------
This project includes code from the commons-lang project, licensed under the
Apache License Version 2.0. See http://jakarta.apache.org/commons/license.html
