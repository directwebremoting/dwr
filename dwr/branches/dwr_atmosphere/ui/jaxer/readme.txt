
About the DWR+Jaxer integration
-------------------------------

The Jaxer directory includes a set of specializations to DWR that help it to be
run in a Jaxer context.

DWR+Jaxer is different from DWR in 2 important respects:
- Much of the complexity of DWR configuration is to restrict what classes the
  Internet can access. When DWR is fronted by Jaxer, these restrictions are no
  longer needed, and configuration can be greatly simplified. (For this reason
  it is important that a DWR server designed to respond to Jaxer requests is
  fire-walled from the Internet)
- Synchronous communication has always been regarded as a bad idea over the net
  because it freezes the client while the server is responding. In the single-
  threaded JavaScript environment, this can be a significant glitch in user
  interface. Inside a corporate LAN, latencies are significantly reduced.


About the Resources
-------------------

Currently the DWR+Jaxer outputs are built using the standard DWR ant build file.
The 'jaxer' target creates the following resources in DWR/target/ant/jaxer:
- jaxer-dwr.jar:
  This is the jar file that users will put into their WEB-INF/lib directory on
  the Java server.
- web.xml:
  This is an example of the changes needed to WEB-INF/web.xml (also on the Java
  server)
- demoServer.war:
  This is a simple demonstration web application that includes both of the files
  above and a trivial test client. If users want an easy way to see what
  DWR+Jaxer does, they should start by deploying this file to their Java server
  and buy deploying demoRemoting.zip to the Jaxer server.
- demoRemoting.zip:
  Partner to demoServer.war for deploying to the Jaxer server. Unzip this
  archive into $JAXER_HOME/public. (If you have downloaded the source or checked
  out from SVN, then you can symlink $JAXER_HOME/public/demoRemoting to
  $DWR/jaxer/jaxer-web
- readme.txt: This file


How to get started with DWR+Jaxer
---------------------------------

1. In your Aptana_Jaxer/public directory, create a directory called demoRemoting,
   then download and unzip demoRemoting.zip, into this directory.
   You should now have 2 files:
   - Aptana_Jaxer/public/demoRemoting/index.html
   - Aptana_Jaxer/public/demoRemoting/frameworkPatch.js
   You probably won't need the second, see note below

   (Alternatively if you have checked out the DWR source from the SVN repo at
   http://svn.directwebremoting.org/dwr then you can symlink to
   dwr/ui/jaxer/customDemo/demoRemoting from Aptana_Jaxer/public/demoRemoting)

2. Download demoServer.war and deploy it into your application server

3. Browse to http://localhost:8081/demoRemoting

If you get the message:
  Jaxer error processing request: JavaScript Error: "Jaxer.dwr is undefined"
  Then you are running against a version of Jaxer that does not contain the
  Jaxer/DWR integration.
  The preferred solution is to upgrade Jaxer, however if that fails you can also
  make a small edit to demoRemoting/index.html. See the comments in that file.

If your application server is not on the same machine, or is running on a
different port, then you will need to edit Jaxer.dwr.pathToDwrServlet to point
at the deployment of a web application containing jaxer-dwr.jar
See demoRemoting/index.html (around line 16) for more details.
