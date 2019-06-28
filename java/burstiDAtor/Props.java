package burstiDAtor;

import java.util.TreeMap;
import java.util.Vector;
import java.math.BigDecimal;

public class Props {
    final static String FIELDSEP = "\t", LINESEP = "\n", PAIRSEP = ":",
            DOUBLEFORMAT = "%.5f";

    Vector<String> f;
    TreeMap<String, Object> m;

    public Props() {
        f = new Vector<String>();
        m = new TreeMap<String, Object>();
    }

    public boolean hasKey(String k) {
        return m.containsKey(k);
    }

    public void addKey(String k) {
        if (!hasKey(k)) {
            f.add(k);
            m.put(k, null);
        }
    }

    public void put(String k, double v) {
        addKey(k);
        m.put(k, v);
    }

    public void put(String k, int v) {
        addKey(k);
        m.put(k, v);
    }

    public void put(String k, Object v) {
        addKey(k);
        m.put(k, v);
    }

    public Double getD(String k) {
        return hasKey(k) ? ((Double) m.get(k)) : Double.NaN;
    }

    public Integer getI(String k) {
        return hasKey(k) ? ((Integer) m.get(k)) : Integer.MIN_VALUE;
    }

    public String getS(String k) {
        return ((String) m.get(k));
    }



    public Object get(String k) {
        return m.get(k);
    }

    public void append(Props p) {
        for (String k : p.keyList()) {
            put(k, p.get(k));
        }
    }

    public String toString() {
        return f + " : " + m;
    }

    public Vector<String> keyList() {
        return new Vector<String>(f);
    }

    public String getHeader(String sep, Vector<String> headers) {
        return __getHL(sep, true, headers);
    }

    public String getLine(String sep, Vector<String> headers) {
        return __getHL(sep, false, headers);
    }

    public String getHeader(Vector<String> headers) {
        return getHeader(FIELDSEP, headers);
    }

    public String getLine(Vector<String> headers) {
        return getLine(FIELDSEP, headers);
    }

    public String getHeader() {
        return getHeader(f);
    }

    public String getLine() {
        return getLine(f);
    }

    /**
     * Helper function that either returns the header or a data line
     *
     * @param sep
     * @param isheader
     * @param headers
     * @return
     */

    private String __getHL(String sep, boolean isheader, Vector<String> headers) {
        StringBuffer b = new StringBuffer();
        int n = headers.size();
        if (n > 0) {
            for (int k = 0; k < n; k++) {
                String key = headers.get(k);
                String val = isheader ? key : valString(key);
                b.append(val);
                if (k + 1 < n) {
                    b.append(sep);
                }
            }
        }
        return b.toString();
    }

    /**
     * Returns a clone of this object
     */
    public Props clone() {
        Props p = new Props();
        for (String k : keyList()) {
            p.put(k, get(k));
        }
        return p;
    }

    /**
     * Returns a subset of all properties
     *
     * @param fs
     *            fields to keep (null or empty list return all fields)
     * @return new property object
     */
    public Props filter(String... fs) {
        if (fs == null || fs.length == 0 || (fs.length == 1 && fs[0] == null)) {
            return clone();
        }

        Props p = new Props();
        for (String k : keyList()) {
            if (fs == null) {
                p.put(k, get(k));
            } else {
                for (String f : fs) {
                    if (f.equals(k)) {
                        p.put(k, get(k));
                        break;
                    }
                }
            }
        }
        return p;
    }

    public double getNumeric(String key) {
        if (!isNumeric(key)) {
            return Double.NaN;
        }
        Object o = get(key);
        if (o instanceof Integer) {
            int oi = (Integer) o;
            return ((double) oi);
        } else if (o instanceof Double) {
            return ((Double) o);
        } else {
            throw new RuntimeException("Unable to convert to numeric" + o);
        }
    }

    public boolean isNumeric(String key) {
        Object o = get(key);
        return o != null && (o instanceof Integer || o instanceof Double);
    }

    public String valString(String key) {
        Object o = get(key);
        if (o == null) {
            return "";
        }
        if (o instanceof Integer) {
            return "" + ((Integer) o);
        } else if (o instanceof Double) {
            double d = (Double) o;
            return Double.isNaN(d) ? "" : String.format(DOUBLEFORMAT, d);
        } else {
            return o.toString();
        }
    }

    public String getPairTable() {
        return getPairTable(PAIRSEP);
    }

    public String getPairTable(String pairsep) {
        StringBuffer b = new StringBuffer();
        for (String k : keyList()) {
            b.append(k + pairsep + valString(k) + LINESEP);
        }
        return b.toString();
    }

    public static Vector<String> getBestHeaders(Vector<Props> ps) {
        if (ps == null || ps.size() == 0) {
            return null;
        }

        Vector<String> best = ps.get(0).keyList();
        for (Props p : ps) {
            if (p.keyList().size() > best.size()) {
                best = p.keyList();
            }
        }
        return best;
    }

    public static String getFullTable(Vector<Props> ps) {
        Vector<String> headerfields = getBestHeaders(ps);
        return getFullTable(headerfields, ps);
    }

    public static String getFullTable(Vector<Props> ps, String... hs) {
        Vector<String> hv = new Vector<String>();
        for (String h : hs) {
            hv.add(h);
        }
        return getFullTable(hv, ps);
    }

    public static String getFullTable(Vector<String> headerfields,
            Vector<Props> ps) {
        if (ps == null || ps.size() == 0 || headerfields == null) {
            return null;
        }
        StringBuffer b = new StringBuffer();

        Props p0 = ps.get(0);

        b.append(p0.getHeader(FIELDSEP, headerfields) + LINESEP);
        for (Props p : ps) {
            b.append(p.getLine(FIELDSEP, headerfields) + LINESEP);
        }
        return b.toString();
    }

    /**
     * Computes summary statistics
     *
     * @param ps
     *            Vector of properties over which summary stats are computed
     * @param ws
     *            Which stats must be computed ("mu" for mean, "md" for median)
     * @return Properties with summary statistics
     */

    public static Props getSummaryStats(Vector<Props> ps, String... ws) {
        Props p = new Props();
        int n = ps.size(); // number of samples
        if (ws == null || ws.length == 0) {
            return null;
        }
        int nw = ws.length;
        if (n > 0) {
            Props p0 = ps.get(0);
            Vector<String> ks = p0.keyList();
            int nk = ks.size(); // number of keys

            for (int i = 0; i < nk; i++) { // for each key
                String key = ks.get(i);
                Vector<Double> vals = new Vector<Double>();
                for (int j = 0; j < n; j++) {
                    double v = ps.get(j).getNumeric(key);
                    if (!Double.isNaN(v)) {
                        vals.add(v);
                    }
                }
                if (vals.size() == n) {
                    for (int k = 0; k < nw; k++) {
                        String w = ws[k];
                        double stat = Utils.getStat(w, vals);
                        p.put(w + "_" + key, stat);
                    }
                }
            }
        }
        return p;
    }
}
