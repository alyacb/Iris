
package ide;

import graphs.DistributionManager;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import ui_general_utils.EnterKeyListener;

/**
 *
 * @author alyacarina
 */
public class Inputter extends Panel {
    
    private GraphLeveller view;
    private DistributionManager boss;
    private TextArea console;
    
    public Inputter(){
        super();
        boss = new DistributionManager();
        view = new GraphLeveller(boss);
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());
        add("Center", view);
        console = new TextArea("", 1, 25, TextArea.SCROLLBARS_NONE);
        console.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                view.refresh();
                console.setText("");
            }
        });
        add("South", console);
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("MemoryNode Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new Inputter());
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
    
}
