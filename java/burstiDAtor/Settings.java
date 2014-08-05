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

        put("supported_neuron_types","DA,5HT");
        put("neuron_type",getS("supported_neuron_types").split(",")[0]);

        put("fieldsep", "\t");
        put("linesep", "\n");
        put("doubleformat", "%.5f");
        put("rndupdur", 10d);
        put("shortsummaryfields", "filename,recDur,recDurRndUp,avgSpFreq,"
                + "nBuOrNada,avgBuFreq,avgBuFreq60,"
                + "pctSpInBu,mu_nSp,mu_interSp");
        put("inputext", ".txt");
        put("summaryappend", "_summary.txt");
        put("shortsummaryappend", "_short_summary.txt");
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
