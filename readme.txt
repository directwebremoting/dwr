
DWR
---
These files comprise the DWR project. For more information see:
- http://www.directwebremoting.org/
To find out the version number for this release, see:
- core/impl/main/java/dwr-version.properties

About the Repository
--------------------
The source to DWR is held in the DWR subversion repository:
- http://svn.directwebremoting.org/

There are a number of projects closely related to DWR, held in the same
repository:
- drapgen: A method of generating reverse ajax proxies
- testdwr: A set of pre-configured test environments
- librarydwr: Source .zip files referred to from DWR jar files
- battleships: A multi-player web game that can be built in an hour
- blabberone: A demo of a twitter clone that uses reverse-ajax. Used for
              demonstrating security and comet. Contains intentional insecurity!

If you have write permission to the repository then you will need to use https.

Building DWR
------------
DWR is primarily built using ant. The important ant targets are:
- clean: Removes target files and .DS_Store files left around by OSX
- jar: Create a new dwr.jar file
- war: Create a clean unpacked demo war file for tomcat/jetty/etc
- package: Build a set of output .zip files for distribution

The DWR build system can be adapted to create outputs containing different
modules (see Terminology) so it is simple to create a dwr.jar file that
contains exactly the modules that you need. See the 'modules' property and the
execution of the 'build' macrodef.

The ant outputs are stored in dwr/target/ant.

Terminology
-----------
Module: DWR is made up of a number of modules. The idea is that these modules
are as independent as possible. Spring integration is an example of a module.
The modules are grouped into 5 areas: core, protocol, serverside, ui, noncla
- core: The heart of DWR
  - api: All the important interfaces and classes for users and integrators
  - impl: Implementations of the interface points, of use most to DWR developers
  - legacy: For backwards compatibility
- protocol: A set of ways of talking to the network
- serverside: Integrations with various server-side technologies
- ui: Integrations with various user interface technologies
- noncla: Code for which we do not have a CLA agreement

Each module may contain a number of source trees. One for test, one for demos,
one for the main sources and maybe one for generated code. The module may also
contain a web directory to be merged into an output war file.

Tags used in source tree
------------------------
PERFORMANCE:
  This code is not optimal, however we are delaying tweaking until we know that
  performance is an issue.
TODO:
  Something needs completing.
JDK*:
  When we move the default support level from one JDK to another, we will need
  to alter this code.
SERVLET*:
  When we move the default servlet spec level from one version to another, we
  will need to alter this code.

Refactoring Notes
-----------------
Refactorings that would be a good idea:
- Currently convert is dependent on dwrp, but it should be the other way around
  The Maps and List Converters are different to the others and should be part of
  DWRP. The other converters should take different parameters:
    Object convertInbound(Class<?> destinationType, JavaScriptTypeInformation sourceType, String data) throws ConversionException;  
- Converters create an entire duplicate outbound tree which wastes a lot of
  memory. We should be able to pre-parse the tree and allow the converters to
  write out directly.
    void convertOutbound(Object value, StringBuilder assignCodes, StringBuilder buildCodes, StringBuilder declareCodes, OutboundOptions opts)
    class OutboundOptions {
        isStrictJson;
        isMultiplyReferenced;
        getAssignCodeForChild(Object child);
        getAssignCode;
    }
- ProtocolConstants is in the wrong place and would be a good place to start in
  and refactoring because it is inherently part of DWRP
- Extract an spi from the api and ensure that non-core modules depend on spi
  and not impl
- Call/Calls do 2 jobs, they store the raw data about the requested method call
  and the results of deciding what actual java.lang.Method should be called.
  This results in bizarreness like the need to store exceptions if the parse
  failed, which in turn causes difficulty in exception handling, and the
  possibility that the wrong sequence of actions will be taken in handling the
  call (as was the case with JSON-RPC).
  Phases of a call (this documentation should be copied somewhere):
  Decode the request: Create a batch that describes what the user was asking
    for. This ensures that the request is well formed. Errors decoding the
    request will result in 'go away' error messages. i.e. "I can't understand
    anything that you say" as opposed to "I can't do X".
  Validate the batch: This checks to see that the request is allowed, that the
    resource has been exported, that the parameters make sense for the method in
    question, the the reply can be exported to the user, etc. Errors from this
    phase can be more helpful, however they may be security sensitive, so care
    should be taken.
  Execute: Make the call. Errors at this stage are created by the called
    application.
 
