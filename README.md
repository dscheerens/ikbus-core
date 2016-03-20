I/K-bus core abstractions
=========================

This package contains the core abstractions for establishing communication links with [I-bus and K-bus vehicle networks](http://e38.org/bussystem.pdf) found in various car models (primarily BMW).
The abstractions provided by this package include: packets, messages, parsers, module descriptors and vehicle network profiles.
Furthermore a set of I/O interfaces and classes are provided for setting up a communication link to read and write I/K-bus packets using generic `InputStream` and `OutputStream` objects.

By itself this package only offers the foundation for building applications that process I/K-bus packets.
To actually decode/parse packets and to obtain their semantics you need an additional package.
Soon I will publish an `ikbus-bmw` package that provides an I/K-bus network profile for BMW models [E38](https://en.wikipedia.org/wiki/BMW_7_Series_%28E38%29), [E39](https://en.wikipedia.org/wiki/BMW_5_Series_%28E39%29), [E46](https://en.wikipedia.org/wiki/BMW_3_Series_%28E46%29), and [E53](https://en.wikipedia.org/wiki/BMW_X5_%28E53%29).
As soon as this package is published I will add a link to that repository.

In addition you may want your application to communicate with the I/K-bus network of your vehicle.
For this you will need a physical interface like [Rolf Resler's IBUS interface](http://www.reslers.de/IBUS/).
This IBUS interface adds a serial/COM port to your computer, which can be used to read and write data to/from the I/K-bus network of your vehicle.
The `ikbus-jssc-connector` package (published soon) provides a `SerialPortIKBusConnection` class that can be used to read and write I/K-bus packets from a serial port.

Usage
-----

_** NOTE: This package is currently not yet available on Maven. I will fix this a.s.a.p. **_

To use this package in your application add the following Maven dependency:
```xml
<dependency>
	<groupId>net.novazero.ikbus</groupId>
	<artifactId>ikbus-core</artifactId>
	<version>1.0.0</version>
</dependency>
```

Links
-----
* [I-BUS Inside](http://web.comhem.se/mulle2/IBUSInsideDRAFTREV5.pdf) - Great introduction of the I-bus in the BMW E39. 
* [BMW bus systems](http://e38.org/bussystem.pdf) - Gives an overview of various bus systems used in BMW vehicles from 1991 to 2001.
* [IBUS interface](http://www.reslers.de/IBUS/) - A place where you can buy an USB and RS232 interface to connect to the I/K-bus of your vehicle.
* [NavCoder](http://www.navcoder.com/) - A Windows application to reprogram various BMW I-bus devices.
