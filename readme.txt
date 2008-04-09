
DWR
---
These files comprise the DWR project. For more information see:
- http://www.directwebremoting.org/

To find out the version number for this release, see:
- core/impl/main/java/dwr-version.properties


About the source
----------------
The source to DWR is held in the DWR subversion repository:
- http://svn.directwebremoting.org/

There are a number of projects closely related to DWR, held in the same
repository:
- drapgen: A method of generating reverse ajax proxies
- dwr-test: A set of pre-configured test environments
- library: Source .zip files referred to from DWR jar files
- battleships: A multi-player web game that can be built in an hour

DWR is primarily build using ant. The important ant targets are:
- clean: Removes target files and .DS_Store files left around by OSX
- standard: Build a set of output .zip files for distribution
- war: Create a clean unpacked war file for tomcat/jetty/etc
The DWR build system can be adapted to create outputs containing different
modules (see Terminology) so it is simple to create a dwr.jar file that
contains exactly the modules that you need. See the 'war' target and the
execution of the 'build' macrodef.

Terminology
-----------
Module: DWR is made up of a number of modules. The idea is that these modules
are as independent as possible. Spring integration is an example of a module.
The modules are grouped into 5 areas: core, protocol, serverside, ui, noncla
- core: The heart of DWR
- protocol: A set of ways of talking to the network
- serverside: Integrations with various server-side technologies
- io: Integrations with various user interface technologies
- noncla: Any code for which we do not have a distribution agreement

Each module may contain a number of source trees. One for test, one for demos,
one for the main sources and maybe one for generated code. The module may also
contain a web directory to be merged into an output war file.


Tags used in source tree
------------------------

PERFORMANCE:
  This code is not optimal, however we are delaying tweaking until we know that
  performance is an issue
TODO:
  Something needs completing
JDK*:
  When we move the default support level from one JDK to another, we will need
  to alter this code
