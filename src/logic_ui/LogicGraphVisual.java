package logic_ui;

import java.awt.BorderLayout;
import java.awt.Panel;
import javax.swing.JFrame;
import logic.LogicManager;

/**
 *
 * @author alyacarina
 */
public class LogicGraphVisual extends Panel {
    
    private final LogicManager boss;
    private final LogicGrapher logiGraph;
    
    public LogicGraphVisual(LogicManager boss){
        this.boss = boss;
        this.logiGraph = new LogicGrapher(boss);
        initialize();
    }
    
    private void initialize(){
        setLayout(new BorderLayout());
        
        add("South", new ExpressionComm(boss, logiGraph));
        add("Center", new LogicGrapher(boss));
    }
    
    public static void main(String[] args){
        JFrame lookAtMe = new JFrame("LogicNode Grapher");
        lookAtMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lookAtMe.setLayout(new BorderLayout());
        
        LogicGraphVisual lgv = new LogicGraphVisual(new LogicManager());
        lookAtMe.add(lgv);
        lookAtMe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lookAtMe.setVisible(true);
    }
    
}
