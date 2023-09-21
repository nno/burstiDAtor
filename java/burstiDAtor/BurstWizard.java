package burstiDAtor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class BurstWizard {
    /*
     * public static void processDirectory(String dir) throws Exception {
     * processDirectory(new File(dir)); }
     */

    /**
     * Run the wizard using a previously selected directory
     */
    public static void processDirectory(File dir, BurstiDAtor parent)
            throws Exception {
        Vector<File> files = getBurstFiles(dir);
        int n = files.size();

        if (n == 0) {
            JOptionPane.showMessageDialog(null, "No files found in " + dir);
            return;
        }

        boolean showProgress = (parent != null);

        Settings settings = Settings.getInstance();

        String path = dir.getPath();
        String parent_dir = dir.getParent();

        boolean writeGraphs = settings.getS("output").equals("graphs");

        settings.put("last_source_dir", parent_dir);

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String t = ft.format(new Date());
        String type = settings.getS("neuron_type");

        String pf = settings.getS("allfilesprefix");
        String append = settings.getS("summaryappend");
        String shortappend = settings.getS("shortsummaryappend");

        String prefix = String.format("%s%s%s%s_%s", path, File.separator, pf,
                t, type);
        String fnout = prefix + append;
        String shortfnout = prefix + shortappend;
        String msg = n + " files found\nOutput will be written in:\n" + fnout
                + "\nContinue?";

        if (JOptionPane.showConfirmDialog(null,
                msg) != JOptionPane.OK_OPTION) {
            return;
        }

        if (showProgress) {
            parent.setProgress(0, n + 1);
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
                File fileout = Bursts.getCanonicalOutputFile(file);
                String report = b.getFullReport();
                Utils.writeToText(fileout, report);

                Props p = b.getStats(st);
                props.add(p);

                line += sp.length() + " spikes in";
            }

            if (writeGraphs) {
                AutoCorr ac = new AutoCorr(sp);
                File acfileout = Utils.getCanonicalOutputFile(file,
                        "autocorrappend");
                BufferedImage imga = ac.getPlot();
                ac.writeOutputFile(acfileout, imga);

                Density density = new Density(sp);
                File densityfileout = Utils.getCanonicalOutputFile(file,
                        "densityappend");
                BufferedImage imgd = density.getPlot();
                ac.writeOutputFile(densityfileout, imgd);
            }
            line += " " + file;
            // System.out.println(line);

            if (showProgress) {
                parent.setProgress(i + 1, n + 1);
            }
        }

        String fullsummary = Props.getFullTable(props);
        Utils.writeToText(new File(fnout), fullsummary);

        final String[] shortsummaryfields = Settings.getInstance()
                .getS("shortsummaryfields").split(",");
        String shortsummary = Props.getFullTable(props, shortsummaryfields);
        Utils.writeToText(new File(shortfnout), shortsummary);

        if (showProgress) {
            parent.setProgress(0, 0);
        }
    }

    /**
     * Run the wizard
     */
    public static void burstDirectoryWizard(String rootdir,
            boolean showChooser,
            BurstiDAtor parent) throws Exception {
        File d = (rootdir == null) ? null : new File(rootdir);
        if (showChooser) {
            JFileChooser chooser = new JFileChooser(d);
            chooser.setDialogTitle("Select directory with WAVMK txt files");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                d = chooser.getSelectedFile();
            }
        }

        if (d != null) {
            processDirectory(d, parent);
        }
    }

    public static void burstDirectoryWizard(String rootdir, BurstiDAtor parent)
            throws Exception {
        burstDirectoryWizard(rootdir, true, parent);
    }

    public static void burstDirectoryWizard(BurstiDAtor parent)
            throws Exception {
        Settings s = Settings.getInstance();
        String dir = null;
        if (s.hasKey("last_source_dir")) {
            dir = s.getS("last_source_dir");
        }
        burstDirectoryWizard(dir, parent);
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

    public static void main(String... unused) throws Exception {
        String d = "/Users/nick/organized/201_bursts/exampledata/VTA 2 day control";
        d = "/Users/nick/burstidator";

        File folder = new File(d);
        File files[] = folder.listFiles();
        // Search .png
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.getName().endsWith(".png")) {
                // and deletes
                f.delete();
            }
        }

        // d=null;
        BurstWizard.burstDirectoryWizard(d, false, null);
    }
}
