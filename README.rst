| Code_ | Bugs_ | Forum_ | License_ | Contact_

.. _Code : http://github.com/frgomes/domino-osgi-sample
.. _Bugs : http://github.com/frgomes/domino-osgi-sample/issues
.. _Forum : http://github.com/frgomes/domino-osgi-sample/wiki
.. _License : http://opensource.org/licenses/BSD
.. _Contact : http://github.com/~frgomes
.. _`OSGi`: http://www.osgi.org/
.. _`Karaf`: http://karaf.apache.org/
.. _`Domino`: http://github.com/domino-osgi/domino/

----

This project contains a sample application for `Domino`_, a scala library which
aims to make `OSGi`_ more idiomatic for Scala developers


Requirements
============

* Java 8
* Scala 2.11.7


For the impatient
=================

::

    $ git clone http://github.com/frgomes/domino-osgi-sample
    $ cd domino-osgi-sample
    $ sbt clean package osgiBundle


   
BND complains on ``moduleB``, but if you inspect the MANIFEST.MF in the .JAR file, you
will see that 'apparently' if was populated properly by BND. Only 'apparently' as we
will see below, since ``moduleB`` fails to start in `Karaf`_:

::

    $ karaf
            __ __                  ____      
           / //_/____ __________ _/ __/      
          / ,<  / __ `/ ___/ __ `/ /_        
         / /| |/ /_/ / /  / /_/ / __/        
        /_/ |_|\__,_/_/   \__,_/_/         
    
      Apache Karaf (4.0.0)
    
    Hit '<tab>' for a list of available commands
    and '[cmd] --help' for help on a specific command.
    Hit '<ctrl-d>' or type 'system:shutdown' or 'logout' to shutdown Karaf.
    
    karaf@root()> bundle:install file:///home/rgomes/workspace/domino-osgi-sample/moduleA/target/scala-2.11/modulea_2.11-0.1-SNAPSHOT.jar
    Bundle ID: 57
    karaf@root()> bundle:install file:///home/rgomes/workspace/domino-osgi-sample/moduleB/target/scala-2.11/moduleb_2.11-0.1-SNAPSHOT.jar
    Bundle ID: 58
    karaf@root()> start 57
    Bundle started: com.github.frgomes.domino.modulea
    karaf@root()> start 58
    Error executing command: Error executing command on bundles:
    Error starting bundle58: Unable to resolve com.github.frgomes.domino.moduleb [58](R 58.0): missing requirement [com.github.frgomes.domino.moduleb [58](R 58.0)] osgi.wiring.package; (&(osgi.wiring.package=domino)(version>=1.1.0)(!(version>=2.0.0))) Unresolved requirements: [[com.github.frgomes.domino.moduleb [58](R 58.0)] osgi.wiring.package; (&(osgi.wiring.package=domino)(version>=1.1.0)(!(version>=2.0.0)))]


Known issues
============

*The build fails!* at the moment. This is somewhat "intentional" at this point.
We are still working on this issue.


Support
=======

Please find links on the top of this page.
