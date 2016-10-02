
package logic;

import graphs.MemoryNode;
import java.util.ArrayList;

/**
 *
 * @author alyacarina
 */
public class LogicNode extends MemoryNode {
    
    // LogicManager class ensures that all neighbors are LogicNodes
    
    private final ArrayList<Formula> logic_trees;
    
    public LogicNode(int id, Formula phi) {
        super(new ArrayList<>(), id);
        logic_trees = new ArrayList<>();
        logic_trees.add(phi);
        mouse_x = (int)(Math.random()*300);
        mouse_y = (int)(Math.random()*300);
    }
    
    MemoryNode seekByFormula(Formula sought, ArrayList<Integer> to_ignore) {
        for(Formula logic_tree: logic_trees){
            if(Formula.equivalent(logic_tree, sought)){
                feed();
                return this;
            }
        }
        
        to_ignore.add(getId());
        
        for(MemoryNode neighbor: getNeighbors()){
            if (!to_ignore.contains(neighbor.getId())){
                MemoryNode temp = ((LogicNode) neighbor).seekByFormula(sought, to_ignore);
                if(temp!=null){
                    return temp;
                }
            }
        }
        
        // Not found
        return null;
    }
    
    public Formula getFormula(){
        return logic_trees.get(0);
    }
    
    public void addFormula(Formula phi){
        logic_trees.add(phi);
    }
    
    @Override
    public String toString(){
        return logic_trees.toString();
    }
}
