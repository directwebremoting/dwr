
See www/index.html for more information.

Issues to resolve
-----------------
- better javadoc
- Demo
- Source download
- Some form of bean marshalling
- More testing


Recent changes
--------------
Added execute using XMLHttpRequest, or a fallback to iframe. (Joe Walker)
Changed compilations to include 1.3 VMs. Not tested on anything but 1.4/5 though (Dennis Graham)
Tweak to make server responses clearer in demo pages (Joe Walker)
Separated engine.js to make changing it easier (Joe Walker)
Fixed a bug that caused the errorHandler to be called whatever (Dave Rooney)
Fixed a bug that failed to cut up the allowed parameter properly (Daniel Wunsch)
Added an ant build file (Joe Walker)
Execute static methods without creating bean (suggested by Nils Kilden-Pedersen)
I've added more documentation to the home page about ExecutionContext
    Access to HttpServletRequest and associated classes is now possible using
    uk.ltd.getahead.dwr.ExecutionContext. For example:
    req = ExecutionContext.getExecutionContext().getHttpServletRequest();
Fixed caching issues (Joe Walker)
Broke the main servlet down a bit because it was getting too big (Joe Walker)


About role membership
---------------------
I approve anyone to be an observer, but generally deny requests for developer
access preferring more controlled access to CVS. My standard reply is:
Hi,
I have generaly worked on open source projects using a patch system rather than very open CVS which often ends up with much wasted time spent on resolving developer conflicts, and I'd like to use that model on this project too.
Thanks,
Joe.
