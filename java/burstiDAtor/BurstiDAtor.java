package burstiDAtor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BurstiDAtor extends JFrame implements ActionListener {
    private static final long serialVersionUID = 7782961898610924142L;

    final static String AUTHORS = "Oosterhof, N.N. & Oosterhof, C.A. (2012-2014)";

    final static String VERSION = "0.10", NAME = "BurstiDAtor",
            DESC = "A lightweight discharge analysis program for " 
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
    final static String USE = String.format("If you have used this program "
            + "for a scientific publication, please cite:"
            + "\n %s %s, %s (version %s)", AUTHORS, NAME, DESC, VERSION);

    JButton runWizard, settings, about, quit;

    public BurstiDAtor() {
        runWizard = addButton("Wizard");
        settings = addButton("Settings");
        about = addButton("About");
        quit = addButton("Quit");
        setLayout(new FlowLayout());
        setVisible(true);
        setSize(400, 60);
        setTitle("BurstiDAtor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JButton addButton(String n) {
        JButton b = new JButton(n);
        add(b);
        b.addActionListener(this);
        return b;
    }

    public void actionPerformed(ActionEvent e) {
        Object s = e.getSource();
        String msg = null;
        if (s == runWizard) {
            try {
                BurstWizard.burstDirectoryWizard(null);
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
