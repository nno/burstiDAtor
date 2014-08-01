package burstiDAtor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class BurstWizard {
	public static void processDirectory(String dir) throws Exception {
		processDirectory(new File(dir));
	}

    /**
     * Run the wizard using a previously selected directory
     */
	public static void processDirectory(File dir) throws Exception {
		Vector<File> files = getBurstFiles(dir);
		int n = files.size();

		if (n == 0) {
			JOptionPane.showMessageDialog(null, "No files found in " + dir);
			return;
		}

		Settings s = Settings.getInstance();
		String path = dir.getPath();

		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
		String t = ft.format(new Date());

		String pf = s.getS("allfilesprefix");
		String append = s.getS("summaryappend");
		String shortappend = s.getS("shortsummaryappend");

		String prefix = String.format("%s%s%s%s", path, File.separator, pf, t);
		String fnout = prefix + append;
		String shortfnout = prefix + shortappend;
		String msg = n + " files found\nOutput will be written in:\n" + fnout
				+ "\nContinue?";
		if (JOptionPane.showConfirmDialog(null, msg) != JOptionPane.OK_OPTION) {
			return;
		}

		Vector<Props> props = new Vector<Props>();
		Bursts.StatType st = Bursts.StatType.COLTABLE;
		for (int i = 0; i < n; i++) {
			File file = files.get(i);
			Spikes sp = new Spikes(file);
			String line = "[ " + (i + 1) + " / " + n + " ] ";
			if (sp.length() == 0) {
				line += "no spikes, skipping";
			} else {
				Bursts b = new Bursts(sp);
				File fileout = Bursts.getCanonicalOuputFile(file);
				String report = b.getFullReport();
				Utils.writeToText(fileout, report);

				Props p = b.getStats(st);
				props.add(p);

				line += sp.length() + " spikes in";
			}
			line += " " + file;
			System.out.println(line);

		}

		String fullsummary = Props.getFullTable(props);
		Utils.writeToText(new File(fnout), fullsummary);

		final String[] shortsummaryfields = Settings.getInstance()
				.getS("shortsummaryfields").split(",");
		String shortsummary = Props.getFullTable(props, shortsummaryfields);
		Utils.writeToText(new File(shortfnout), shortsummary);
	}

    /** 
     * Run the wizard
     */
	public static void burstDirectoryWizard(String rootdir) throws Exception {
		File d = null;
		if (rootdir == null) {

			JFileChooser chooser = new JFileChooser(d);
			chooser.setDialogTitle("Select directory with WAVMK txt files");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				d = chooser.getSelectedFile();
			}
		} else {
			d = new File(rootdir);
		}
		if (d != null) {
			processDirectory(d);
		}
	}

	public static Vector<File> getBurstFiles(File dir) {
		if (!dir.isDirectory()) {
			return null;
		}
		Settings settings = Settings.getInstance();
		String ext = settings.getS("inputext").toLowerCase();
		String append = settings.getS("summaryappend");

		Vector<File> fs = new Vector<File>();
		String path = dir.getPath();
		for (String s : dir.list()) {
			String sl = s.toLowerCase();

			if (sl.endsWith(ext) && !(sl.endsWith(append))) {
				fs.add(new File(path + File.separator + s));
			}
		}
		return fs;
	}

	public static void main(String... _) throws Exception {
		String d = "/Users/nick/organized/201_bursts/exampledata/VTA 2 day control";
        d="/Users/nick/Downloads/Wizard test";
		// d=null;
		BurstWizard.burstDirectoryWizard(d);
	}
}
