
package ide;

import core.Iris;
import java.awt.BorderLayout;
import java.awt.Panel;
import javax.swing.JFrame;

/**
 *
 * @author alyacarina
 */
public class IDE extends Panel {
    
    private final Consolation c;
    private final FileManager fm;
    private Iris brains;
    
    public IDE(){
        c = new Consolation();
        brains = new Iris();
        c.setIris(brains);
        fm = new FileManager(brains, this);
        initialize();
    }
    
    public void notifyOfBrain(){
        brains = fm.getIris();
        c.setIris(brains);
    }
    
    private void initialize(){
        setLayout(new BorderLayout());
        add("Center", c);
        add("West", fm);
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("MemoryNode Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new IDE());
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
    
}
