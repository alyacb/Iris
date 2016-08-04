
package graph_ui;

import graph_ui.Grapher;
import graphs.MemoryManager;
import java.awt.BorderLayout;
import java.awt.Panel;
import javax.swing.JFrame;

/**
 *
 * @author alyacarina
 */
public class MemoryNodeUI extends Panel {
    private final MemoryManager memory;
    public MemoryNodeUI(){
        memory = new MemoryManager();
        Grapher g = new Grapher(memory);
        MemoryManagerConsole m = new MemoryManagerConsole(memory, g);
        
        initialize(g, m);
    }
    
    private void initialize(Grapher g, MemoryManagerConsole m){
        setLayout(new BorderLayout());
        add("Center", g);
        add("South", m);
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("Complete MemoryNode UI");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        lookAtMe.add("Center", new MemoryNodeUI());
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
}
