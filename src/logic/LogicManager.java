
package logic;

import graphs.MemoryManager;
import graphs.MemoryNode;
import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */
public class LogicManager extends MemoryManager {
    
    public LogicManager(){
        number_of_nodes = 0;
        root = new LogicNode(0, null);
        root.mouse_x = 200;
        root.mouse_y = 100;
    }
    
    @Override
    public void addMemoryNode(int destination_id){
        // OBSOLETE
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void addMemoryNode(MemoryNode node, int destination_id){
        // OBSOLETE
        throw new UnsupportedOperationException();
    }
    
    public void addLogicNode(Formula phi){
        LogicNode ln = (LogicNode) ((LogicNode)root).seekByFormula(phi, new ArrayList<>());
        if(ln != null){
            ln.addFormula(phi);
        } else {
            LogicNode n = new LogicNode(getNextId(), phi);
            ArrayList<Integer> x = new ArrayList<>();
            x.add(n.getId());
            addLogicNodeByEntailment(n, (LogicNode)root, x);
        }
    }
    
    public void addLogicNode(LogicNode node, int destination_id){
        super.addMemoryNode(node, destination_id);
    }
    
    @Override
    public void addMemoryNodes(ArrayList<MemoryNode> memories, int destination_id){
        // OBSOLETE
        throw new UnsupportedOperationException();
    }
    
    public void addLogicNodes(ArrayList<LogicNode> memories, int destination_id){
        ArrayList<MemoryNode> cp = new ArrayList<>();
        cp.addAll(memories);
        super.addMemoryNodes(cp, destination_id);
    }
    
    @Override
    public void addStringNode(String s, int destination_id){
        // OBSOLETE
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void addStringNodes(String[] s, int destination_id){
        // OBSOLETE
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void learnWords(String[] words){
        // OBSOLETE
        throw new UnsupportedOperationException();
    }

    // Add formulas to all statements that entail them
    private void addLogicNodeByEntailment(LogicNode n, LogicNode ln, 
                                          ArrayList<Integer> forbidden) {
        forbidden.add(ln.getId());
        
        if(ln.getId() == 0 || (new Implication((Formula)n.getFormula(), 
                                 (Formula)ln.getFormula())).isValid()){
            ln.addNeighbor(n);
        }
        
        for(MemoryNode mn: ln.neighbors){
            if(!forbidden.contains(mn.getId())){
                addLogicNodeByEntailment(n, (LogicNode) mn, forbidden);
            }
        }
    }
}
