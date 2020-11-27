package burstiDAtor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Density extends Histogram {
    public Density(Spikes spikes) {
	super();
	int min_nbins = Settings.getInstance().getI("density_min_bins");
	initialize_bins(min_nbins);

	int n_spikes = spikes.length();
	if (n_spikes < 1) {
	    return;
	}

	Spike start_spike = spikes.get(0);
	Spike end_spike = spikes.get(n_spikes-1);

	String step_str = Settings.getInstance().getS("density_step");
	SpikeDelta step = SpikeDelta.fromString(step_str);

	Spike interval_start = start_spike.divideFloor(step);
	Spike interval_end = end_spike.divideCeil(step);
	System.out.println("" + start_spike + "/" + end_spike);
	SpikeDelta full_duration = new SpikeDelta(interval_start, interval_end);
	System.out.println("" + full_duration + "/" + interval_start + "/" + interval_end);
	int n_intervals = getInterval(interval_start, full_duration, n_spikes, end_spike) + 1;
	int[] interval_counts = new int[n_intervals];


	for (int i=0; i<spikes.length(); i++) {
	    Spike spike = spikes.get(i);
	    int interval = getInterval(interval_start, full_duration, n_spikes, spike);
	    interval_counts[interval]++;
	}

	for (int j=0; j<n_intervals; j++) {
	    addOneToBin(interval_counts[j]);
	}


    }


    public void initialize_bins(int min_nbins) {
	bins.add((double) 0);
	ensure_enough_bins(min_nbins);
    }

    public int getInterval(Spike first_spike, SpikeDelta duration,
	    				int n_spikes, Spike spike) {
	SpikeDelta delta = new SpikeDelta(first_spike, spike);
	int bin = delta.multiplyBy(n_spikes).divideFloor(duration);
	return bin;
    }

    public void addOneToBin(int bin) {
	ensure_enough_bins(1 + bin);
   	counts.set(bin, 1 + counts.get(bin));
    }

    public void ensure_enough_bins(int n_bins) {
	while (true) {
	    int max_bin = bins.size();
	    if (n_bins < max_bin) {
		return;
	    }
	    bins.add((double) max_bin);
	    counts.add(0);
   	}
    }

    public BufferedImage getPlot() {
	return super.getPlot("ISI");
    }


    public static void main(String... unused) throws Exception {
	String d = "/Users/nick/burstidator/";
/*
	String[] fns = { "neuron 1 651542.txt",
			 "neuron 2 653286.txt",
			 "Neuron 3 653284.txt" };


	String[] fns = {"500 180.txt",
			"250 180.txt"
				};
*/
	String[] fns = {"1s 180.txt",
			"500 180.txt"
			};

	for (int i = 0; i < fns.length; i++) {

	    String fn = d + fns[i];
	    File f = new File(fn);
	    Spikes spikes = new Spikes(f);
	    Density density = new Density(spikes);

	    BufferedImage img = density.getPlot("ISIs");

	    try {
		// Save as PNG
		File file = new File(fn.replace(".txt", ".png"));
		ImageIO.write(img, "png", file);
		System.out.println(density.counts);

	    } catch (IOException e) {
	    }

	}
    }

}
