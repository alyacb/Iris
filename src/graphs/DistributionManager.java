
package graphs;

import java.util.ArrayList;
import statistics_analysis.DataSet;
import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
public class DistributionManager extends MemoryManager {
    
    private static final String BAD_NODE_TYPE = 
            "Can only add DistributionNodes to this graph.";
    private static final String NO_DISTRIBUTION = 
            "DistributionNodes must have a set distribution.";
    
    // Constructor:
    public DistributionManager(){
        super();
        root = new DistributionNode(null, new ArrayList<>(), 0);
        root.mouse_x = 200;
        root.mouse_y = 100;
        root.setData(new DataSet());
        newest = root;
    }
    
    // DEPRECATED
    @Override
    public void addMemoryNode(int destination_id) {
        throw new UnsupportedOperationException(NO_DISTRIBUTION);
    }
    
    @Override
    public void addMemoryNode(MemoryNode node, int destination_id){
        if(!(node instanceof DistributionNode)) 
            throw new UnsupportedOperationException(BAD_NODE_TYPE);
        super.addMemoryNode(node, destination_id);
    }
    
    @Override
    public void addMemoryNodes(ArrayList<MemoryNode> memories, int destination_id){
        for(MemoryNode node: memories){
            if(!(node instanceof DistributionNode)) 
                throw new UnsupportedOperationException(BAD_NODE_TYPE);
        }
        super.addMemoryNodes(memories, destination_id);
    }
    
    public void addDistributionNode(Distribution d, int destination_id){
        DistributionNode temp = new DistributionNode(d, new ArrayList<>(), getNextId());
        temp.mouse_x = (int) (Math.random()*1000);
        temp.mouse_y = (int) (Math.random()*600);
        addMemoryNode(temp, destination_id);
    }
    
    public void addDistributionNode(Distribution d){
        addDistributionNode(d, 0);
    }
}
