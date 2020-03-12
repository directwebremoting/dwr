# DWR - Direct Web Remoting

DWR is a Java library that enables Java on the server and JavaScript in a browser to interact and call each other as simply as possible.
DWR is Easy Ajax for Java.

Website http://directwebremoting.org \
Forum https://discourse.dojo.io/c/dwr-users

Old mailing lists Mailman archive:\
http://mail.dojotoolkit.org/mailman/listinfo/dwr-users \
http://mail.dojotoolkit.org/mailman/listinfo/dwr-dev

Old mailing lists Nabble archive:\
http://dwr.2114559.n2.nabble.com/DWR-Users-f2114559.html \
http://dwr.2114559.n2.nabble.com/DWR-Dev-f5409447.html

## Building DWR

DWR is primarily built using ant. The important ant targets are:
* clean: Removes target files and .DS_Store files left around by OSX
* jar: Create a new dwr.jar file
* war: Create a clean unpacked demo war file for tomcat/jetty/etc
* package: Build a set of output .zip files for distribution

The DWR build system can be adapted to create outputs containing different
modules (see Terminology) so it is simple to create a dwr.jar file that
contains exactly the modules that you need. See the 'modules' property and the
execution of the 'build' macrodef.

The ant outputs are stored in dwr/target/ant.

## Terminology

Module: DWR is made up of a number of modules. The idea is that these modules
are as independent as possible. Spring integration is an example of a module.
The modules are grouped into four areas: core, protocol, serverside, ui
* core: The heart of DWR
  * api: All the important interfaces and classes for users and integrators
  * impl: Implementations of the interface points, of use most to DWR developers
  * legacy: For backwards compatibility
* protocol: A set of ways of talking to the network
* serverside: Integrations with various server-side technologies
* ui: Integrations with various user interface technologies

Each module may contain a number of source trees. One for test, one for demos,
one for the main sources and maybe one for generated code. The module may also
contain a web directory to be merged into an output war file.

## Licensing information

DWR is a subproject of Dojo which is a member of the JS Foundation.

© 2005 [JS Foundation](https://js.foundation/) under [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) license.

DWR includes derivative works under other copyright notices and distributed according to the terms of their respective licenses, 
please see [COPYRIGHT.txt](COPYRIGHT.txt) and [LICENSE.txt](LICENSE.txt) for details.
