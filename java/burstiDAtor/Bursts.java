package burstiDAtor;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFileChooser;

public class Bursts {
    public static String SUMMARYAPPEND = "_summary.txt",
            SHORT_SUMMARYAPPEND = "_short" + SUMMARYAPPEND;

    public static enum StatType {
        FULL, PAIRTABLE, COLTABLE
    };

    private Spikes sps;

    public Bursts(Spikes s) {
        this.sps = s;
    }

    public Bursts(File f) {
        this(new Spikes(f));
    }

    public Bursts(String fn) {
        this(new Spikes(fn));
    }

    /**
     * Identifies bursts
     * 
     * @return list of bursts detected
     */
    public Vector<Burst> getBursts() {
        Vector<Burst> bs = new Vector<Burst>();
        Settings set = Settings.getInstance();

        // burst detection parameters
        String prefix=set.getS("neuron_type")+"_";

        double spstartinterval = set.getD(prefix+"maxburststart");
        double spcontinueinterval = set.getD(prefix+"maxburstcontinue");
        int minspinburst = set.getI(prefix+"minspikesinburst");

        // ensure that burst continue interval not less than burst
        // start interval
        if (spcontinueinterval < spstartinterval) {
            throw new RuntimeException("Not supported intervals");
        }

        int nsp = sps.length(); // number of spikes

        if (nsp == 0) {
            // no spikes, return
            return bs;
        }

        double prevonset = -1000000d;// Double.MIN_VALUE;
        Burst curburst = null;

        // traverse over spikes
        for (int i = 0; i < nsp; i++) {
            double d = sps.get(i);
            double delta = d - prevonset;

            if (curburst == null && delta < spstartinterval) {
                // start a new burst
                curburst = new Burst();
                curburst.add(prevonset);
                curburst.add(d);
            } else if (curburst != null && delta < spcontinueinterval) {
                // continue current burst
                curburst.add(d);
            } else {
                // add a burst if enough spikes
                if (curburst != null
                        && curburst.getStats().getI("nSp") >= minspinburst) {
                    bs.add(curburst);
                }
                curburst = null;
            }
            prevonset = d;
        }

        // add last burst if at the very end of the recording interval
        if (curburst != null && curburst.getStats().getI("nSp") >= minspinburst) {
            bs.add(curburst);
        }

        return bs;
    }

    public String toString() {
        return getBursts().toString();
    }

    public Vector<Props> getBurstStats() {
        return getBurstStats("nBu", "SpFreq", "BuDur", "interSp");
    }

    public Vector<Props> getBurstStats(String... fs) {
        Vector<Props> ps = new Vector<Props>();
        for (Burst b : getBursts()) {
            ps.add(b.getStats(fs));
        }
        return ps;
    }

    public String getIndividualBurstsTable() {
        String SEP = Props.FIELDSEP;
        String NL = Props.LINESEP; // newline
        Vector<Burst> bursts = getBursts();
        StringBuffer b = new StringBuffer();
        int n = bursts.size();
        if (n > 0) {
            for (int k = 0; k < n; k++) {
                Burst burst = bursts.get(k);
                Props s = burst.getStats();
                if (k == 0) {
                    // header
                    b.append("BuNr" + SEP + s.getHeader() + NL);
                }
                // line with data, prepended by spike number
                b.append((k + 1) + SEP + s.getLine() + NL);
            }
        } else {
            return "* no bursts found *";
        }
        return b.toString();
    }

    public String getSummaryPairTable() {
        StringBuffer b = new StringBuffer();
        Props stats = getStats(StatType.PAIRTABLE);
        b.append(stats.getPairTable());
        return b.toString();
    }

    public String getFullReport() {
        return "=== Individual bursts ===\n" + getIndividualBurstsTable()
                + "\n=== Summary of bursts ===\n" + getSummaryPairTable();
    }

    public String[] getFullReportLines() {
        return getFullReport().split("\n");
    }

    public static Props individualBurstWizardProps(String rootdir) {
        Props props = new Props();
        File f = rootdir == null ? null : new File(rootdir);

        JFileChooser fc = new JFileChooser(f);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File filein = fc.getSelectedFile();
            File fileout = getCanonicalOuputFile(filein);
            props.put("filein", filein);
            if (fileout == null) {
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileout = fc.getSelectedFile();
                    props.put("fileout", fileout);
                    return props;
                }
            } else {
                props.put("fileout", fileout);
                return props;
            }
        }
        return null;

    }

    /**
     * Gets an output file name for a burst file
     */
    public static File getCanonicalOuputFile(File inputFile) {
        Settings settings = Settings.getInstance();

        String ext = settings.getS("inputext");
        String s = inputFile.getPath();
        if (!s.toLowerCase().endsWith(ext.toLowerCase())) {
            return null;
        }
        
        int pos=s.length()-ext.length();
        String s_cut = s.substring(0, pos);
        String output_ext = "_" + settings.getS("neuron_type") + 
                                        settings.getS("summaryappend");
        File outputFile=new File(s_cut + output_ext);

        return outputFile;
    }

    /**
     * Computes
     * 
     * @param tp
     *            statistics type (one of Bursts.StatType.
     *            {PAIRTABLE,FULL,COLTABLE}
     * @return Props object with statistics of spikes and bursts
     */

    public Props getStats(StatType tp) {
        if (tp == null) {
            tp = StatType.FULL;
        }
        Props p = new Props();
        int nsp = sps.length();

        if (tp == StatType.FULL || tp == StatType.PAIRTABLE) {
            p.put("date", (new Date()).toString());
            String desc = String.format("%s v%s by %s", BurstiDAtor.NAME,
                    BurstiDAtor.VERSION, BurstiDAtor.AUTHORS);
            p.put("analysis", desc);
        }

        String fn = sps.getName();
        p.put("filename", fn);

        if (nsp == 0) {
            return p;
        }

        p.put("firstSp", sps.get(0));
        p.put("lastSp", sps.get(nsp - 1));
        double recdur = sps.get(nsp - 1) - sps.get(0);
        p.put("recDur", recdur);

        double rnddur = Settings.getInstance().getD("rndupdur");

        // round up to 10 second bins, or whatever is the setting
        // of "rndupdur"
        double recdurrnd = Math.round(recdur / rnddur + 1 / (2 * rnddur))
                * rnddur;
        p.put("recDurRndUp", recdurrnd);
        p.put("nSp", nsp);
        p.put("avgSpRate", recdur / ((double) nsp));
        p.put("avgSpRateRndUp", recdurrnd / ((double) nsp));
        p.put("avgSpFreq", ((double) nsp) / recdur);
        p.put("avgSpFreqRndUp", ((double) nsp) / recdurrnd);

        Vector<Burst> bs = getBursts();
        int nbs = bs.size();
        p.put("nBu", nbs);

        String nbs_or_nada="" + (nbs==0 ? "" : nbs);
        p.put("nBuOrNada", nbs_or_nada);

        if (nbs == 0) {
            return p;
        }

        double firstbucenter = Double.NaN;
        double lastbucenter = Double.NaN;

        if (nbs > 1) {
            Props pb0 = bs.get(0).getStats((String) null); // first burst
            Props pbX = bs.get(nbs - 1).getStats((String) null); // last burst

            firstbucenter = pb0.getD("center");
            lastbucenter = pbX.getD("center");
        }

        int spinbucount = 0; // spikes in burst
        double prevburstend = 0; // end of previous burst
        double interburstsum = 0; // sum of interburst intervals
        for (int k = 0; k < nbs; k++) {
            Burst b = bs.get(k);
            Props s = b.getStats((String) null);
            spinbucount += s.getI("nSp");
            double burststart = s.getD("firstSp");
            double burstend = s.getD("lastSp");

            if (k > 0) {
                interburstsum += burststart - prevburstend;
            }

            prevburstend = burstend;
        }
        double pctinburst = 100 * ((double) spinbucount) / ((double) nsp);
        p.put("pctSpInBu", pctinburst);

        p.put("interBuIvl", interburstsum / (((double) nbs) - 1));
        p.put("firstToLastBuCentered", lastbucenter - firstbucenter);
        p.put("CycleBu", (lastbucenter - firstbucenter) / (((double) nbs) - 1));

        p.put("avgBuFreq", ((double) nbs) / recdur);
        p.put("avgBuFreqRndUp", ((double) nbs) / recdurrnd);

        p.put("avgBuFreq60", (60.0 * (double) nbs) / recdur);

        // compute stats across bursts
        final String[] statfields = { "nSp", "SpFreq", "BuDur", "interSp" };
        final String[] summarytps = { "mu", "md", "std" };
        Props sumstats = Props.getSummaryStats(getBurstStats(statfields),
                summarytps);
        p.append(sumstats);

        return p;
    }
}
