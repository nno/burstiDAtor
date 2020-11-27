package burstiDAtor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AutoCorr extends Histogram {
    public AutoCorr(Spikes spikes) {
	super();
	int n_spikes = spikes.length();

	Settings settings = Settings.getInstance();
	SpikeDelta min_isi = SpikeDelta.fromString(settings.getS("autocorr_min"));
	SpikeDelta max_isi = SpikeDelta.fromString(settings.getS("autocorr_max"));
	SpikeDelta binwidth = SpikeDelta.fromString(settings.getS("autocorr_binwidth"));

	int n_bins = max_isi.subtract(min_isi).divideCeil(binwidth);

	for (int i = 0; i <= n_bins; i++) {
	    bins.add((double) i);
	    if (i<n_bins) {
	    	counts.add(0);
	    }
	}

	if (n_spikes<2) {
	    return;
	}

	for (int i = 0; i < n_spikes; i++) {
	    for (int j = i + 1; j < spikes.length(); j++) {
		SpikeDelta delta = new SpikeDelta(spikes.get(i), spikes.get(j));
		int bin = getBin(min_isi, binwidth, delta);
		boolean too_small = bin < 0;
		boolean too_large = bin >= n_bins;
		boolean keep = !(too_small || too_large);
		if (keep) {
		    counts.set(bin, 1 + counts.get(bin));
		}
		if (too_large) {
		    break;
		}
	    }
	}
    }

    public static int getBin(SpikeDelta t_onset, SpikeDelta binwidth, SpikeDelta t) {
	int bin = t.subtract(t_onset).divideFloor(binwidth);
	return bin;
    }

    public BufferedImage getPlot() {
	return super.getPlot("time (ms)");
    }


    public static void main(String... unused) throws Exception {
	String d = "/Users/nick/tmp/";

	String[] fns = { "neuron 1 651542.txt", "neuron 2 653286.txt", "Neuron 3 653284.txt" };

	for (int i = 0; i < fns.length; i++) {

	    String fn = "/Users/nick/tmp/" + fns[i];
	    File f = new File(fn);
	    Spikes spikes = new Spikes(f);
	    AutoCorr ac = new AutoCorr(spikes);

	    BufferedImage img = ac.getPlot();

	    try {
		// Save as PNG
		File file = new File("/Users/nick/tmp/neuron" + (i + 1) + ".png");
		ImageIO.write(img, "png", file);

	    } catch (IOException e) {
	    }

	}
    }
}
