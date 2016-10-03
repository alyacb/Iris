
package statistics_distributions;

import graphs.DistributionManager;
import graphs.DistributionNode;
import graphs.MemoryNode;
import java.util.ArrayList;
import java.util.Iterator;
import statistics_analysis.DataSet;
import statistics_analysis.PValueGenerator;
import statistics_analysis.DistributionGenerator;

/**
 *
 * @author alyacarina
 */
public class GraphDistribution extends Distribution {

    private DistributionManager graph;
    private final static String BAD_METHOD = "Not computable.";
    private ArrayList<Distribution> track;
    
    public GraphDistribution() {
        super("Graph Distribution", new double[]{});
        graph = new DistributionManager();
        track = new ArrayList();
        validate();
    }

    // Original Methods
    
    // Reset tracker
    public void clearTrack(){
        track = new ArrayList();
    }
    
    private DistributionNode huntForDistribution(double x){
        if(track.isEmpty()){
            // search for first node with a high-enough p-value to not reject hypothesis
        } else {
            // search among neighbors of latest node for best distribution
            Iterator i = track.iterator();
            while(i.hasNext()){
                DistributionNode link = (DistributionNode)i.next();
                Distribution d = link.getDistribution();
                DataSet ds = (DataSet) link.getData();
                ds.addDatum(x);
                PValueGenerator pvg = new PValueGenerator(ds);
                if(pvg.getPValue(d, 0) > 0.1){ // distribution hypothesis not rejected
                    link.addConfirmedDatum(x);
                    return link;
                }
            }
        }
        
        return null;
    }
    
    // Overrides
    
    @Override
    public double getMean() {
        // should never be called
        throw new UnsupportedOperationException(BAD_METHOD);
    }

    @Override
    public double getVariance() {
        // should never be called
        throw new UnsupportedOperationException(BAD_METHOD);
    }

    @Override
    public double f(double x) {
        DistributionNode target = huntForDistribution(x);
        if(target == null) return 0;
        track.add(target.getDistribution());
        double fx = 1;
        
        for(Distribution d: track){
            fx*=d.f(x);
        }
        
        return fx;
    }

    @Override
    protected double est_param_impl(int i, DataSet data) {
        // should never be called
        throw new UnsupportedOperationException(BAD_METHOD);
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
        // nothing to do, no args
    }
    
    // hunt for, and update, DistributionNodes that need updating
    private void goodNight(DistributionNode current, ArrayList<Integer> visited){
        visited.add(current.getId());
        
        if(current.shouldUpdate()) {
            DistributionGenerator dg = new DistributionGenerator((DataSet)current.getData());
            current.setDistribution(dg.generateBestDistribution());
        }
        
        for(MemoryNode neighbor: current.getNeighbors()){
            if(!visited.contains(neighbor.getId())){
                goodNight((DistributionNode) neighbor, visited);
            }
        }
        
    }
    
    // Sends Iris to sleep- reorganizes data, reevaluates distributions
    public void goodNight(){
        goodNight((DistributionNode) graph.root, new ArrayList());
        
        graph.sleep();
        
    }
    
}
