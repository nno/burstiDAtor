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
            DESC = "a neural spike/burst detector";
    final static String GPL = "This program is free software; "
            + " you can redistribute "
            + "it and/or modify it under terms of the \nGNU General "
            + "Public License as published by the Free Software "
            + "Foundation; either version 2\nof the License, or "
            + "(at your option) any later version.\n\nThis program "
            + "is distributed in the hope that it will be useful, "
            + "but WITHOUT ANY WARRANTY; \nwithout even the implied "
            + "warranty of MERCHANTABILITY or FITNESS FOR A "
            + "PARTICULAR \nPURPOSE.  See the GNU General Public "
            + "License for more details.\n\nYou should have received "
            + "a copy of the GNU General Public License along with "
            + "this program; \nif not, write to the Free Software "
            + "Foundation, Inc., 51 Franklin Street, Fifth Floor, "
            + "Boston, MA\n02110-1301  USA";
    final static String LICENSE = String.format("Copyright %s\n\n%s", AUTHORS,
            GPL);
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
