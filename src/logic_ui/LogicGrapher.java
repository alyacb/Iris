
package logic_ui;

import graph_ui.Grapher;
import graphs.MemoryNode;
import java.util.Timer;
import java.util.TimerTask;
import logic.LogicManager;

/**
 *
 * @author alyacarina
 */
public class LogicGrapher extends Grapher {
    // INEFFICIENT- solve
    Timer refresher;
    
    public LogicGrapher(LogicManager boss) {
        super(boss, false, "LogicGrapher");
        refresher = new Timer();
        refresher.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 500);
    }
    
    // Overrides...
    @Override
    public void addNode(int mouse_x, int mouse_y){
        // nothing
    }
    
    @Override
    public void knitNodes(MemoryNode temp){
        // nothing
        selected = temp;
    }
    
}
