About
------

Logger is a autoscrolling log tail web-application. Just drop it in your java 
webcontainer and point your browser at /log. It will automatically find and 
scroll your tomcat log.

Running
-------

To start the server after download

$ mvn jetty:run

then point your browser to http://localhost:9090/log

To create a war, type

$ mvn package

and the war will be created in /target.

Extending
---------

Extending the logger is simple and consists of writing a new LogFinder
implementation which needs to locate the log file you are interested in.
If the log you are looking for uses java.util.logging.Logger you will most
likely be able to reuse most of the TomcatLogFileFinder.
 
Install the logfinder as a service in the class ServiceInitializer and you 
should be good to go. The webapplication defaults to the tomcat service so 
to find your new service point your browser to /log/<service>