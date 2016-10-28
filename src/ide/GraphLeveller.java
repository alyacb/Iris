
package ide;

import graph_ui.Grapher;
import graphs.DistributionManager;
import graphs.DistributionNode;
import graphs.MemoryNode;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.util.ArrayList;
import statistics_distributions.GraphDistribution;

/**
 *
 * @author alyacarina
 */

/**
 * Class that manages Graph being visualized
 *      used for multi-tiered graphs
 */

public class GraphLeveller extends Panel {
    
    private final DistributionManager source; // Graph source
    private ArrayList<DistributionNode> path;
    private DistributionNode current_tier; // Graph root node currently being looked at
    private final Grapher graph_plane;
    
    public GraphLeveller(DistributionManager source){
        setLayout(new BorderLayout());
        this.source = source;
        current_tier = (DistributionNode)source.root;
        path = new ArrayList<>();
        graph_plane = new Grapher(source) 
        {
            @Override
            public void addNode(int mouse_x, int mouse_y){
                // do nothing, disable user adding
            }
            
            @Override
            public void knitNodes(MemoryNode temp){
                // do nothing, disable user knitting
            }
            
            @Override
            public void overrideableAction(){
                // Attempt to descend to lower tier of graph
                if(current_tier.getId()!=getSelected().getId()
                   && getSelected() instanceof DistributionNode
                   && ((DistributionNode)getSelected()).getDistribution() instanceof GraphDistribution){
                    path.add(current_tier);
                    current_tier = (DistributionNode)source.root;
                    syncTierScope();
                } else if(current_tier.getId() == getSelected().getId() && path.size()>0){ 
                    // rise through tiers
                    current_tier = path.get(path.size()-1);
                    path.remove(current_tier);
                    syncTierScope();
                }
            }
        };
        add(graph_plane);
    }
    
    public void refresh(){
        graph_plane.refresh();
    }
    
    private void syncTierScope(){
        GraphDistribution graphd = (GraphDistribution)current_tier.getDistribution();
        graph_plane.setManager(graphd.getGraph());
        graph_plane.refresh();
    }
    
}
