
package graphs;

import java.util.ArrayList;
import statistics_analysis.DataSet;
import statistics_analysis.DistributionGenerator;
import statistics_distributions.Distribution;
import statistics_distributions.GraphDistribution;

/**
 *
 * @author alyacarina
 */
public class DistributionNode extends MemoryNode {
    
    private Distribution distribution; // distribution attributed to this node
    private boolean needs_update;
    private double suggested_bin_size;
    
    public DistributionNode(Distribution distribution, ArrayList<MemoryNode> neighbors, int id) {
        super(neighbors, id);
        this.distribution = distribution;
        this.datum = new DataSet(); // set 'data' of node to be all 'confirmed' data items
        needs_update = false;
        suggested_bin_size = 0;
    }
    
    public void addConfirmedDatum(double datum){
        needs_update = true;
        ((DataSet)this.datum).addDatum(datum);
    }
    
    public void setDistribution(Distribution d){
        if(needs_update){
            distribution = d;
            needs_update = false;
        }
    }
    
    // TBI 
    // Essential for correct node evaluations
    public void recalculateBestBinSize(){
        DataSet d = (DataSet)datum;
        double r = d.getRange();
        double db = d.getMinGap();
        if(db == 0){
            suggested_bin_size = db;
            return;
        }
        for(double b = 0; b<r; b+=0.001){
            
        }
    }
    
    public double getPreferredBinSize(){
        return suggested_bin_size;
    }
    
    public Distribution getDistribution(){
        return distribution;
    }
    
    public boolean shouldUpdate(){
        return needs_update;
    }
    
    // NOTE: addneighbors allows regular node adding- BAD HACK
    //     need to encapsulate better

    public void update(DistributionManager graph) {
        DistributionGenerator dg = 
                    new DistributionGenerator((DataSet)getData(), graph);
        setDistribution(dg.generateBestDistribution());
        if(distribution instanceof GraphDistribution){
            ((GraphDistribution)distribution).goodNight();
        }
        recalculateBestBinSize();
    }
    
}
