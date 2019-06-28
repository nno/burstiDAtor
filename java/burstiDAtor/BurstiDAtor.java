package burstiDAtor;

import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class BurstiDAtor extends JFrame implements ActionListener {
    private static final long serialVersionUID = 7782961898610924142L;

    final static String AUTHORS = "Oosterhof, N.N. & Oosterhof, C.A. (2012-2019)";

    final static String VERSION = "0.30wo";
    final static String NAME = "burstiDAtor";
    final static String DESC = "a lightweight discharge analysis program for "
                        + "neural extracellular single unit recordings";
    final static String LICENSE_BODY = "Permission is hereby granted, free "
            + "of charge, to any person obtaining a copy of this software\n"
            + "and associated documentation files (the \"Software\"), to "
            + "deal in the Software without restriction, \nincluding without "
            + "limitation the rights to use, copy, modify, merge, publish, "
            + "distribute, sublicense,\nand/or sell copies of the Software, "
            + "and to permit persons to whom the Software is furnished to "
            + "do so,\nsubject to the following two conditions:\n\n(1) The above "
            + "copyright notice and this permission notice shall be included "
            + "in all copies or substantial portions of the Software.\n\n(2)THE "
            + "SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, "
            + "EXPRESS\n OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE "
            + "WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR\n"
            + "PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR "
            + "COPYRIGHT HOLDERS BE LIABLE FOR\n ANY CLAIM, DAMAGES OR OTHER "
            + "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR "
            + "OTHERWISE,\nARISING FROM, OUT OF OR IN CONNECTION WITH THE "
            + "SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.";
    final static String LICENSE = String.format("%s version %s: copyright %s\n\n%s",
	    NAME, VERSION, AUTHORS, LICENSE_BODY);
    final static String URL = "github.com/nno/burstiDAtor";
    final static String USE = String.format("If you use this program "
            + "for a scientific publication, please cite:\n\n"
            + "\t%s %s:\n\t%s (version %s)\n\tavailable from %s",
                AUTHORS, NAME, DESC, VERSION, URL);


    JPanel buttons;
    JButton runWizard, neuronType, output, settings, about, manual, code, quit;
    JProgressBar progressBar;

    public BurstiDAtor() {
        buttons = new JPanel(new FlowLayout());

        runWizard = addButton("Wizard");
        neuronType = addButton("");
        output = addButton("");
        settings = addButton("Settings");
        about = addButton("About");
        manual = addButton("Manual");
        code = addButton("Code");
        quit = addButton("Quit");

        changeNeuronTypeText(0);
        changeOutputText(0);

        progressBar = new JProgressBar();
        setProgress(0, 0);

        setLayout(new GridLayout(2, 1));
        add(buttons);
        add(progressBar);

        setVisible(true);
        setSize(800, 100);
        setTitle("BurstiDAtor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Settings getSettings() {
        return Settings.getInstance();
    }

    public void setProgress(int p, int maximum) {
	boolean visible = maximum==0;

	if (visible) {
	    progressBar.setMaximum(maximum);
	    progressBar.setValue(p);
	}

	progressBar.setVisible(visible);
	System.out.format("progress %d / %d\n", p, maximum );
    }


    private JButton addButton(String n) {
        JButton b = new JButton(n);
        buttons.add(b);
        b.addActionListener(this);
        return b;
    }

    private void setCaption(JButton src_object, String label) {
	src_object.setText(label);
    }


    private void changeButtonText(JButton src_object, String prefix,
	    			String key, String supported_key, int delta) {
	Settings set = getSettings();
        String type = set.getS(key);
        String[] supported = set.getS(supported_key).split(",");

        int n = supported.length;
        for (int i=0; i<n; i++) {
            if (type.equals(supported[i])) {
                int j=(i+delta) % n;
                String new_value = supported[j];
                set.put(key, new_value);
                String label=prefix + new_value;
                setCaption(src_object, label);
                return;
            }
        }

        set.put(key, supported[0]);

    }

    private void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(url));
                } catch (Exception _) {
                    System.err.println("Unable to open " + url);
                }
            }
        }
    }

    private void changeNeuronTypeText(int delta) {
	changeButtonText(neuronType, "type: ",
		"neuron_type", "supported_neuron_types", delta);
    }

    private void changeOutputText(int delta) {
	changeButtonText( output, "",
		"output", "supported_outputs", delta);
    }

    public void actionPerformed(ActionEvent e) {
        Object s = e.getSource();
        String msg = null;
        if (s == runWizard) {
            try {
                BurstWizard.burstDirectoryWizard(this);
            } catch (Exception _) {
                _.printStackTrace();
                msg = "An error has occured: " + _.getMessage();
            }
        } else if (s == settings) {
            msg = Settings.getInstance().getPairTable(": ");
        } else if (s == about) {
            msg = LICENSE + "\n\n" + USE;
        } else if (s == quit) {
            System.exit(0);
        } else if (s == neuronType) {
            changeNeuronTypeText(1);
        } else if (s == output) {
            changeOutputText(1);
        } else if (s == manual) {
            openURL("https://github.com/nno/burstiDAtor/releases/download/" +
                    "v" + VERSION + "/burstiDAtor.pdf");
        } else if (s == code) {
            openURL("https://github.com/nno/burstiDAtor");
        }

        if (msg != null) {
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    /**
     * Start burstidator wizard
     *
     * @param _
     */
    public static void main(String... _) {
        new BurstiDAtor();
    }

}
