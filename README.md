burstiDAtor
===========
A lightweight discharge analysis program for neural extracellular single unit recordings.

Features
--------
- Compute summary statistics for neural spike recordings (e.g. firing frequency, number of spikes, inter-spike interval, number of burst, burst frequency). 
- Burst detection based on criterion of maximum spike intervals to start and continue a burst (80 and 160 ms by default, respectively), as proposed by Grace & Bunney (1984).
- Supports wavemark analysis in Spike2 (Cambridge ELectronic Design, Cambridge, UK) input files.
- Implementations in Matlab and java.
- Free/Open Source Software (MIT License).

Requirements
------------
- The Matlab code requires a working installation of Matlab. Unit test use the xUnit framework
- The java code requires a Java VM, version 1.6 or later.

Getting started
---------------
- Matlab: see the example in matlab/examples.
- Java:
    + For non-programmers: download and run the Java archive (``BurstiDAtor.jar``) from https://github.com/nno/burstiDAtor/releases. 
    + For programmers (requires a Java compiler): compile the sources in one of the following ways:
        * ``make build`` to build Java ``.class`` files.
        * ``make run`` to build Java ``.class`` files and run burstiDAtor.
        * ``make jar`` to build a Java archive (``.jar``) file.
        * Manually run ``javac burstiDAtor/*.java`` in the ``java directory``, then run ``java burstiDAtor/burstiDAtor``. 

Documentation
-------------
- A pdf version of the documentation is available fromhttps://github.com/nno/burstiDAtor/releases. 
- Sources for the documentation are provided in the ``doc`` directory. 
- Documentation can be built in ``pdf`` or ``html`` formats using sphinx-doc (http://sphinx-doc.org/latest/install.html) and the ``Makefile`` (Unix-like systems) or ``make.bat`` (MS Windows systems) in the ``doc`` directory. 

Developers
----------
burstiDAtor is developed by:
- Nikolaas N. Oosterhof
- Chris A. Oosterhof

Contribution guidelines
-----------------------
The preferred way to contribute is through github: https://github.com/nno/burstiDAtor


Citation
--------
If you use burstiDAtor for a scientific publication, please cite:

Oosterhof, N.N. & Oosterhof, C.A. (2013), BurstiDAtor: A lightweight discharge analysis program for neural extracellular single unit recordings. https://github.com/nno/burstiDAtor.

BibTeX:
```
  @misc{burstiDAtor,
    title =	"BurstiDAtor: A lightweight discharge analysis program for neural extracellular single unit recordings",
    author = "N.N. Oosterhof and C.A. Oosterhof",
    year = "2013",
    url = "https://github.com/nno/burstiDAtor",
  }
```


License
-------
MIT License; for details, see the COPYING file.
