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
    Vector<Spike> onsets;

    public Spikes(File f) {
        this.sourcefile = f.getAbsolutePath();

        try {
            BufferedReader r = new BufferedReader(new FileReader(f));
            String line;

            String wavemarker = Settings.getInstance().getS("wavemarker");

            onsets = new Vector<Spike>();
            while ((line = r.readLine()) != null) {
                String[] split = line.split("\t");
                if (split.length < 2)
                    continue;

                if (split[0].equals(wavemarker)) {
                    String onset_string = split[1];
                    Spike onset = Spike.fromString(onset_string);
                    onsets.add(onset);
                }
            }
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Spikes(String fn, double... os) {
        this.sourcefile = fn;

        onsets = new Vector<Spike>();
        for (Double o : os) {
            onsets.add(Spike.fromDouble(o));
        }
    }

    public int length() {
        return onsets.size();
    }

    public Spike get(int i) {
        return onsets.get(i);
    }

    public double recordingDuration() {
        int n = length();
        if (n == 0) {
            return 0.0;
        }
        Spike first = get(0);
        Spike last = get(n - 1);
        return last.subtract(first).doubleValue();
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
        for (Spike onset : onsets) {
            b.append("\n" + onset);
        }
        return b.toString();
    }
}
