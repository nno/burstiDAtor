package burstiDAtor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

/**
 * Represents a set of spike onsets from a txt file
 * 
 * @author nick
 * 
 */
public class Spikes {
    String sourcefile;
    Vector<Double> onsets;

    public Spikes(File f) {
        this.sourcefile = f.getAbsolutePath();
        try {
            BufferedReader r = new BufferedReader(new FileReader(f));
            String line;

            String wavemarker = Settings.getInstance().getS("wavemarker");

            onsets = new Vector<Double>();
            while ((line = r.readLine()) != null) {
                String[] split = line.split("\t");
                if (split.length < 2)
                    continue;

                if (split[0].equals(wavemarker)) {
                    double onset = Double.parseDouble(split[1]);
                    onsets.add(onset);
                }
            }
            r.close();
        } catch (Exception _) {
            _.printStackTrace();
        }
    }

    public Spikes(String fn, double... os) {
        this.sourcefile = fn;
        onsets = new Vector<Double>();
        for (double o : os) {
            onsets.add(o);
        }
    }

    public int length() {
        return onsets.size();
    }

    public double get(int i) {
        return onsets.get(i);
    }

    public Spikes(String fn) {
        this(new File(fn));
    }

    public String getParent() {
        return (new File(sourcefile)).getParent();
    };

    public String getName() {
        return sourcefile == null ? null : (new File(sourcefile)).getName();
    };

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(sourcefile);
        for (double onset : onsets) {
            b.append("\n" + onset);
        }
        return b.toString();
    }
}
