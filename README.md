burstiDAtor
===========
A lightweight discharge analysis program for neural extracellular single unit recordings.

Features
--------
- Compute summary statistics for neural spike recordings (e.g. firing frequency, number of spikes, inter-spike interval, number of burst, burst frequency). 
- Burst detection based on criterion of maximum spike intervals to start and continue a burst (80 and 160 ms by default, respectively). 
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
- Java: compile ``burstiDAtor/*.java``, then run burstiDAtor/burstiDAtor.class. 

Developers
----------
CoSMoMVPA is developed by:
- [Nikolaas N. Oosterhof](http://haxbylab.dartmouth.edu/ppl/nno.html).
- [Chris A. Oosterhof].

Contribution guidelines
-----------------------
The preferred way to contribute is through github: https://github.com/nno/burstiDAtor


Citation
--------
If you use burstiDAtor for a scientific publication, please cite:

Oosterhof, N.N. & Oosterhof, C.A. (2012-2014), BurstiDAtor: A lightweight discharge analysis program for neural extracellular single unit recordings. https://github.com/nno/burstiDAtor.


License
-------
MIT License; for details, see the COPYING file.
