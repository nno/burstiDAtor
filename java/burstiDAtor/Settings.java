package burstiDAtor;

public class Settings extends Props {
	private static Settings me = null;

	private Settings() {
		super();
		put("DA_maxburststart", .08d);
		put("DA_maxburstcontinue", .16d);
		put("DA_minspikesinburst", 2);

		put("5HT_maxburststart", .02d);
		put("5HT_maxburstcontinue", .02d);
		put("5HT_minspikesinburst", 2);

		put("RTN_maxburststart", .01d);
		put("RTN_maxburstcontinue", .01d);
		put("RTN_minspikesinburst", 2);

		put("GLU_maxburststart", .016d);
		put("GLU_maxburstcontinue", .016d);
		put("GLU_minspikesinburst", 2);

		put("autocorr_min", "0.00000");
		put("autocorr_max", "2.00000");
		put("autocorr_binwidth", "0.01000");

		put("density_min_bins", 10);
		put("density_step", "1.000000");

		put("supported_outputs", "graphs,no graphs");
		put("output", getS("supported_outputs").split(",")[0]);

		put("supported_neuron_types", "DA,5HT,RTN,GLU");
		put("neuron_type", getS("supported_neuron_types").split(",")[0]);

		put("fieldsep", "\t");
		put("linesep", "\n");
		put("doubleformat", "%.5f");
		put("rndupdur", 10d);
		put("shortsummaryfields", "filename,recDur,recDurRndUp,avgSpFreqRndUp,"
					+ "nBuOrNada,avgBuFreqRndUp,avgBuFreq60RndUp,CVI,"
					+ "pctSpInBu,mu_nSp,mu_interSp");
		put("inputext", ".txt");
		put("summaryappend", "_summary.txt");
		put("shortsummaryappend", "_short_summary.txt");

		put("autocorrappend", "_autocorr.png");
		put("densityappend", "_density.png");

		put("allfilesprefix", "allfiles_");
		put("wavemarker", "\"WAVMK\"");
	}

	/**
	 * Return singleton instance
	 */
	public static Settings getInstance() {
		if (me == null) {
			me = new Settings();
		}
		return me;
	}

}
