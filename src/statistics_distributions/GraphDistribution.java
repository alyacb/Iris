
package statistics_distributions;

import graphs.DistributionManager;
import graphs.DistributionNode;
import graphs.MemoryNode;
import java.util.ArrayList;
import statistics_analysis.DataSet;
import statistics_analysis.PValueGenerator;

/**
 *
 * @author alyacarina
 */

/** <!-- NOTE TO DEV -->
 * Need to implement correctional features.
 *  A) Iris needs to be able to adjust her base graph when she is wrong.
 * i.e. LEARN from mistakes. Figure out a system to allow such changes,
 * that is not purely statistical in nature. 
 * <!-- IDEA --> input expected vs. input produced-
 * Iris can look at what she knows (or doesn't) to figure out if she should have taken a different path
 * to the response, or if she just needs to add this response to the end of this path (train of thought).
 *  B) Graphs need to reshuffle on sleep to be rooted at the most popular
 * node, or rather the node linked to the most 'popular' nodes- the most 
 * interconnected node, for faster traversal. Typically one & same.
**/

public class GraphDistribution extends Distribution {

    private final DistributionManager graph;
    private final static String BAD_METHOD = "Not computable.";
    private int total_frequency;
    private DistributionNode current_scope; // node of graph we're analyzing from
    private double track;
    private double last_datum;
    
    public GraphDistribution() {
        super("Graph Distribution", new double[]{});
        graph = new DistributionManager();
        current_scope = (DistributionNode) graph.root;
        total_frequency = graph.getTotalFrequency();
        track = -1;
        validate();
    }

    // Methods
    
    // Reset tracker, for when a brand new, unrelated search needs to be made
    public void clearTrack(){
        current_scope = (DistributionNode)graph.root;
        track = -1;
    }
    
    // Search for first node in graph with a high-enough p-value to not reject hypothesis
    private void seekFirst(DistributionNode node, 
            ArrayList<Integer> visited) {
        PValueGenerator nuisance = new PValueGenerator((DataSet)node.getData());
        visited.add(node.getId());
        if(node.getDistribution() != null){
            double pv = nuisance.getPValue(node.getDistribution(), node.getPreferredBinSize())
                    * node.getNumberOfCalls()/total_frequency;
            if(track*pv>0.05){
                node.addConfirmedDatum(last_datum);
                track*=pv;
                current_scope = node;
                return;
            }
        }
        for(MemoryNode d: node.getNeighbors()){
            if(visited.contains(d.getId())) continue;
            seekFirst((DistributionNode) d, visited);
        }
        current_scope = (DistributionNode) graph.root; // nothing was found
    }
    
    //Look for closest appropriate distribution in the current scope
    private DistributionNode huntForDistribution(double x){
        DistributionNode previous_scope = current_scope;
        if(track == -1){
            track = 1; // start tracking
            current_scope = (DistributionNode)graph.root;
        } 
        
        last_datum = x;
        
        if(current_scope != null) {
            seekFirst(current_scope, new ArrayList());
        }
        
        // link it to previous, if necessary
        //   note that checking for existing neighbors 
        //   with the same ID is done in the memorynode class
        if(previous_scope != current_scope){
            current_scope.addNeighbor(previous_scope);
        }
        
        return current_scope;
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
        if(target == graph.root) return 0; // the existing graph has no facilities
        
        return track;
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
            current.update(graph);
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
        total_frequency = graph.getTotalFrequency();
        graph.sleep();
    }
    
    public DistributionManager getGraph(){
        return graph;
    }

    public void addDistributionNode(Distribution distant) {
        graph.addDistributionNode(distant);
    }

}
