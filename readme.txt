
See www/index.html for more information.

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


License
-------
This project includes code from the commons-lang project, licensed under the
Apache License Version 2.0. See http://jakarta.apache.org/commons/license.html
There is some confusion as to the exact version of the license since the above
statement conflicts with the copyright statement at the top of the source files.
Since it is clear that the Apache organization is changing from version 1.0 to
version 2.0, the latter is being used.
