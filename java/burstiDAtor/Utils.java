package burstiDAtor;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Vector;

public class Utils {
    public static double getStat(String w, Vector<Double> vs) {
        int n = vs.size();

        // Java <1.7 does not support switch statements with strings
        if (w.equals("n")) {
            return n;
        } else if (w.equals("mu")) {
            double sum = 0;
            for (Double v : vs) {
                sum += v;
            }
            return sum / ((double) n);
        } else if (w.equals("md")) {
            Collections.sort(vs);
            int n2 = n / 2;
            if (n % 2 == 0) {
                return .5 * (vs.get(n2 - 1) + vs.get(n2));
            } else {
                return vs.get(n2);
            }
        } else if (w.equals("var")) {
            double m = getStat("mu", vs);

            double ss = 0;
            for (double v : vs) {
                double d = (v - m);
                ss += d * d;
            }

            return ss / (n - 1);
        } else if (w.equals("std")) {
            double var = getStat("var", vs);
            return Math.sqrt(var);

        } else {
            throw new RuntimeException("Unknown stat " + w);
        }
    }

    public static void writeToText(File fileout, String s) throws Exception {
        PrintWriter w = new PrintWriter(new FileWriter(fileout));
        w.write(s);
        w.flush();
        w.close();
    }

    public static File getCanonicalOutputFile(File inputFile, String output_key) {
	Settings settings = Settings.getInstance();

	String ext = settings.getS("inputext");
	String s = inputFile.getPath();
	if (!s.toLowerCase().endsWith(ext.toLowerCase())) {
	    return null;
	}

	int pos = s.length() - ext.length();
	String s_cut = s.substring(0, pos);
	String output_ext = "_" + settings.getS("neuron_type") + settings.getS(output_key);
	File outputFile = new File(s_cut + output_ext);

	return outputFile;
    }
}
