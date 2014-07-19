User manual
***********
This manual describes how to use burstiDAtor to detect bursts in neural spike data, using the criteria of [GB84]_. Using wavemark analysis in Spike2 and burstiDAtor, most relevant discharge characteristics (such as firing rate, burst frequency, spikes per burst, interspike interval, etc) of these can be obtained using the following steps. 

Software requirements
---------------------
Required software to run the steps described in this manual: 

- Java (to run burstiDAtor)
- Spike2
- Microsoft Excel (other spreadsheet programmes can be used as well)


Spike analysis in Spike2
------------------------

* Analyze all neurons in the folder ``recordings`` using the wavemark analysis tool (file;analysis; new wavemark... ;  adjust horizontal & vertical triggers, check the box *make templater* ;  click the *play* button; new channel). 
* After verifying that the wavemark channel reflects the firing activity of the neurons in the recording channel, select the wavemark channel (double-click on it, or select *view* ;  *show/hide channel*).

    + In the folder ``wavemark_analysis``, example wavemark channels are provided for each recordings.

* Select *file* ;  *export as*. 
* Enter a name, and in the *save as type* drop down box, select *text file*; *ok*.
* Enter the interval of which analysis is desired and click ok. 
  + See the folder ``wavemark_output`` for the example wavemark channels exported as ``*.txt`` files.

Most work is done now. To let burstiDAtor do its magic, we only need to tell it which text files we want to analyze. 

Burstidator analysis
--------------------
* Start burstiDAtor
  + usually this involves double-clicking the burstiDAtor ``.jar`` file.
* Click *Wizard*.
* Browse to the folder containing the ``*.txt`` output files from Spike2 (see above).
* Click *open*. BurstiDAtor will display the number of ``*.txt`` files in that folder.
  + it ignores any files ending with ``*_summary.txt``, because the output from burstiDAtor is written to files with such names (see below).
* + it proposes to write its output in that same folder; click yes. 
* Analysis is done almost instantly, and we're done!

For each text file, burstidDAtor has now generated a ``*_summary.txt`` file that contains detailed information regarding the discharge actitivity of each individual neuron. Most importantly however, it has summarized the average discharge activity for each neuron in a ``*_summary.txt`` file and the more convenient ``*_short_summary.txt`` (simply select this file and drag it into Excel).

Summary file format
-------------------
The summary file provides the following information:

======================= ==============================================
label                   meaning
======================= ==============================================
firstSp	                First spike
lastSp                  Last spike 
recDur                  Recording duration
recDurRndUp             Recordign duration rounded up
nSp                     Total number of spikes
avgSpRate               Average spikes (time/spikes)
avgSpRateRndUp          Average spikes rounded up
avgSpFreq               Average spike frequency (Hz)
avgSpFreqRndUp          Average spike frequency 
nBu                     Total number of bursts
pctSpInBu               Percent of spikes in burst
interBuIvl              Inter-burst interval
firstToLastBuCentered   First to last burst centered
CycleBu	                Burst cycle (time/bursts)
avgBuFreq               Average burst frequency (bursts/s)
avgBuFreqRndUp          Average burst frequency rounded up
mu_nSp                  Average number of spikes/burst
md_nSp                  Median number of spikes/burst
std_nSp                 Standard deviation spikes/burst
mu_BuDur                Average burst duration
md_BuDur                Median burst duration
std_BuDur               Standard deviation burst duration
mu_SpFreq               Average firing frequency in bursts (Hz)
md_SpFreq               Median firing frequency in bursts (Hz)
std_SpFreq              Standard deviation firing frequency in bursts 
mu_interSp              Mean inter-spike interval (ISI)
md_interSp              Median ISI
std_interSp             Standard deviation ISI
======================= ==============================================




