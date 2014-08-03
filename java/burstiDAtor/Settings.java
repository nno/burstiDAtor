package burstiDAtor;

public class Settings extends Props {
    private static Settings me = null;

    private Settings() {
        super();
        put("maxburststart", .08d);
        put("maxburstcontinue", .16d);
        put("minspikesinburst", 2);
        put("fieldsep", "\t");
        put("linesep", "\n");
        put("doubleformat", "%.5f");
        put("rndupdur", 10d);
        put("shortsummaryfields", "filename,recDur,avgSpFreq,"
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
