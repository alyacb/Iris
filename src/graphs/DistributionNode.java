
package graphs;

import java.util.ArrayList;
import statistics_analysis.DataSet;
import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
public class DistributionNode extends MemoryNode {
    
    private Distribution distribution; // distribution attributed to this node
    private boolean needs_update;
    
    public DistributionNode(Distribution distribution, ArrayList<MemoryNode> neighbors, int id) {
        super(neighbors, id);
        this.distribution = distribution;
        this.datum = new DataSet();
        needs_update = false;
    }
    
    public void addConfirmedDatum(double datum){
        needs_update = true;
        ((DataSet)this.datum).addDatum(datum);
    }
    
    public void setDistribution(Distribution d){
        distribution = d;
        needs_update = false;
    }
    
    public Distribution getDistribution(){
        return distribution;
    }
    
    public boolean shouldUpdate(){
        return needs_update;
    }
}
