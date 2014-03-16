package burstiDAtor;

import java.util.Vector;

/**
 * Contains onset information of spikes in a burst
 * @author nick
 *
 */
public class Burst {
	Vector<Double> onsets;
	
	/**
	 * Initialize an empty burst
	 */
	public Burst() {
		onsets=new Vector<Double>();
	}
	
	/**
	 * add a spike to the burst
	 * @param onset time of the spike
	 */
	public void add(double onset) {
		onsets.add(onset);
	}
	
	/**
	 * Compute statistics for this burst
	 * @param fs Optional list of property keys to return. If null or empty
	 * then all keys are returned.
	 * @return Props instance containing properties of this burst, with a
	 * subset of the following keys (or all if fs is null or empty):
	 * "nSp" (number of spikes), "firstSp" (onset of first spike), "lastSp"
	 * (onset of last spike), "center" (average of first and last spike),
	 * "BuDur" (duration of the burst), "SpFreq" (spike frequency in the burst),
	 * and "interSp" (interspike interval in the burst).
	 */
	public Props getStats(String ... fs) {
		Props s=new Props();
		int n=onsets.size();
		
		s.put("nSp", n);
		
		double first=n>0?onsets.get(0):Double.NaN;
		double last=n>0?onsets.get(n-1):Double.NaN;
		
		double dur=last-first;
		double freq=((double)n)/dur;
		
		s.put("firstSp",first);
		s.put("lastSp",last);
		s.put("center", .5*(last+first));
		s.put("BuDur",dur);
		s.put("SpFreq",freq);
		s.put("interSp", dur/(double)(n-1));
		
	
		Props r=(fs==null || 
				 fs.length==0 || 
				 (fs.length==1 && fs[0]==null)) ? s : s.filter(fs);
		return r;
	}
	/**
	 * @return string representation of this burst
	 */
	public String toString() { return getStats().toString(); }
	
	/**
	 * @return header representation of this burst
	 */
	public String getHeader() { return getStats().getHeader(); }
	/**
	 * @return single line representation of this burst
	 */
	public String getLine() { return getStats().getLine(); }
	
	
}
