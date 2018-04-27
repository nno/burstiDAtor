package burstiDAtor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Desktop;

import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BurstiDAtor extends JFrame implements ActionListener {
    private static final long serialVersionUID = 7782961898610924142L;

    final static String AUTHORS = "Oosterhof, N.N. & Oosterhof, C.A. (2012-2018)";

    final static String VERSION = "0.21";
    final static String NAME = "burstiDAtor";
    final static String DESC = "a lightweight discharge analysis program for " 
                        + "neural extracellular single unit recordings";
    final static String LICENSE_BODY = "Permission is hereby granted, free "
            + "of charge, to any person obtaining a copy of this software\n"
            + "and associated documentation files (the \"Software\"), to "
            + "deal in the Software without restriction\n, including without "
            + "limitation the rights to use, copy, modify, merge, publish, "
            + "distribute, sublicense,\nand/or sell copies of the Software, "
            + "and to permit persons to whom the Software is furnished to "
            + "do so,\nsubject to the following conditions:\n\nThe above "
            + "copyright notice and this permission notice shall be included "
            + "in all copies or substantial portions of the Software.\n\nTHE "
            + "SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, "
            + "EXPRESS\n OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE "
            + "WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR\n"
            + "PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR "
            + "COPYRIGHT HOLDERS BE LIABLE FOR\n ANY CLAIM, DAMAGES OR OTHER "
            + "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR "
            + "OTHERWISE,\nARISING FROM, OUT OF OR IN CONNECTION WITH THE "
            + "SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.";
    final static String LICENSE = String.format("Copyright %s\n\n%s", AUTHORS,
            LICENSE_BODY);
    final static String URL = "github.com/nno/burstiDAtor";
    final static String USE = String.format("If you use this program "
            + "for a scientific publication, please cite:\n\n"
            + "\t%s %s:\n\t%s (version %s)\n\tavailable from %s", 
                AUTHORS, NAME, DESC, VERSION, URL);

    JButton runWizard, neuronType, settings, about, manual, code, quit;

    public BurstiDAtor() {
        Settings set = Settings.getInstance();
        String type = set.getS("neuron_type");

        runWizard = addButton("Wizard");
        neuronType = addButton("");
        settings = addButton("Settings");
        about = addButton("About");
        manual = addButton("Manual");
        code = addButton("Code");
        quit = addButton("Quit");

        setTypeCaption();

        setLayout(new FlowLayout());
        setVisible(true);
        setSize(700, 60);
        setTitle("BurstiDAtor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Settings getSettings() {
        return Settings.getInstance();
    }

    private void setTypeCaption() {
        neuronType.setText("type: " + getSettings().getS("neuron_type"));
    }

    private JButton addButton(String n) {
        JButton b = new JButton(n);
        add(b);
        b.addActionListener(this);
        return b;
    }

    private void changeNeuronType() {
        Settings set = getSettings();
        String type = set.getS("neuron_type");
        String[] supported = set.getS("supported_neuron_types").split(",");

        int n = supported.length;
        for (int i=0; i<n; i++) {
            if (type.equals(supported[i])) {
                int j=(i+1) % n;
                set.put("neuron_type", supported[j]);
                setTypeCaption();
                return;
            }
        }
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


    public void actionPerformed(ActionEvent e) {
        Object s = e.getSource();
        String msg = null;
        if (s == runWizard) {
            try {
                BurstWizard.burstDirectoryWizard();
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
            changeNeuronType();
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
