| Code_ | Bugs_ | Forum_ | License_ | Changes_ | Contact_

.. _Code : http://github.com/frgomes/domino-osgi-sample
.. _Bugs : http://github.com/frgomes/domino-osgi-sample/issues
.. _Forum : http://github.com/frgomes/domino-osgi-sample/wiki
.. _License : http://opensource.org/licenses/BSD-3-Clause
.. _Changes : http://github.com/frgomes/domino-osgi-sample/CHANGES.rst
.. _Contact : http://github.com/frgomes
.. _`OSGi`: http://www.osgi.org/
.. _`Karaf`: http://karaf.apache.org/
.. _`Domino`: http://github.com/domino-osgi/domino/
.. _`bnd`: http://www.aqute.biz/Bnd/Bnd

----

This project contains a sample application for `Domino`_, a scala library which
aims to make `OSGi`_ more idiomatic for Scala developers.


Requirements
============

* Java 8
* Scala 2.11


For the impatient
=================

* Download and building

::

    $ git clone http://github.com/frgomes/domino-osgi-sample
    $ cd domino-osgi-sample
    $ ./sbt clean package osgiBundle


**NOTE:** `sbt-osig`_ crashes after upgrade to bndlib 3.2.0

* Running Karaf, installing Scala, Domino and the bundles we've just built:

::

    $ karaf
    karaf@root()> bundle:install file:///home/richard/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.11.7.jar
    karaf@root()> bundle:install file:///home/richard/.ivy2/cache/org.scala-lang/scala-reflect/jars/scala-reflect-2.11.7.jar
    karaf@root()> bundle:install file:///home/richard/.ivy2/cache/com.github.domino-osgi/domino_2.11/bundles/domino_2.11-1.1.0.jar
    karaf@root()> bundle:install file:///home/richard/workspace/domino-osgi-sample/moduleA/target/scala-2.11/modulea_2.11-0.1-SNAPSHOT.jar
    karaf@root()> bundle:install file:///home/richard/workspace/domino-osgi-sample/moduleB/target/scala-2.11/moduleb_2.11-0.1-SNAPSHOT.jar
    karaf@root()> start org.scala-lang.scala-library
    karaf@root()> start org.scala-lang.scala-reflect
    karaf@root()> start domino_2.11
    karaf@root()> start com.github.frgomes.domino.modulea
    karaf@root()> start com.github.frgomes.domino.moduleb


**NOTE:** `Karaf`_ fails to start ``com.github.frgomes.domino.moduleb``. This is a know issue.


Known issues
============

1. https://github.com/frgomes/domino-osgi-sample/issues/1

::

    Error executing command: Error executing command on bundles:
    Error starting bundle67: Activator start error in bundle com.github.frgomes.domino.moduleb [67].
