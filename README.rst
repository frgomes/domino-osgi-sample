| Code_ | Bugs_ | Forum_ | License_ | Contact_

.. _Code : http://github.com/frgomes/domino-osgi-sample
.. _Bugs : http://github.com/frgomes/domino-osgi-sample/issues
.. _Forum : http://github.com/frgomes/domino-osgi-sample/wiki
.. _License : http://opensource.org/licenses/BSD-3-Clause
.. _Contact : http://github.com/~frgomes
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
* Scala 2.11.7


For the impatient
=================

::

    $ git clone http://github.com/frgomes/domino-osgi-sample
    $ cd domino-osgi-sample
    $ ./sbt clean package osgiBundle



**NOTE:** `bnd`_ complains on ``moduleB``. `Karaf`_ fails to activate ``moduleB``.


Known issues
============

1. https://github.com/frgomes/domino-osgi-sample/issues/1


Support
=======

Please find links on the top of this page.
