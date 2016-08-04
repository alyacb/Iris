
package graph_ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author alyacarina
 */
public class IrisPrimeRunner {
    public static void main(String[] args) {
        JFrame lookAtMe = new JFrame("Iris Builder");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new IrisPrime());
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
