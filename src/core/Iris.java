
package core;

import graphs.DistributionNode;
import java.io.Serializable;
import java.util.ArrayList;
import statistics_analysis.Composer;
import statistics_distributions.Distribution;
import statistics_distributions.GraphDistribution;

/**
 *
 * @author alyacarina
 */
public class Iris implements Serializable {
    
    private final GraphDistribution memory;
    private ArrayList<Scope> tracker;
    private Composer bach;
    
    public Iris(){
        memory = new GraphDistribution();
        tracker = new ArrayList<>();
        tracker.add(new Scope(memory.getGraph()));
        bach = new Composer(this);
    }
    
    // Convert string input to numbers- BASIC: chars to ints.
    public void placeInput(String input){
        double[] vals = new double[input.length()];
        for(int i=0; i<vals.length; i++){
            vals[i] = input.charAt(i);
        }
        placeInput(vals);
    }
    
    private void placeInput(double[] input){
        for(int i=0; i<input.length; i++){
            Scope current = Scope.clone(tracker.get(i));
            current.setLocale(current.find(input[i]));
            tracker.add(new Scope(current));
        }
        
        // Identify discrepancies, substitute them with the best distribution 
        //  for the job, reinforce existing along path, and insert new nodes
        //  where they do not currently exist
        for(int i=0; i<input.length; i++){
            Scope level = tracker.get(i);
            if(level.getLocale() != null){
                // reinforce
                level.getLocale().feed();
                level.getLocale().addConfirmedDatum(input[i]);
                if(i>0){
                    DistributionNode previousNode = tracker.get(i-1).getLocale();
                    Distribution previousDistribution = previousNode.getDistribution();
                    
                    if(previousDistribution instanceof GraphDistribution){
                        ((GraphDistribution)previousDistribution)
                                .addDistributionNode(level.getLocale().getDistribution());
                    } else {
                        previousDistribution = previousDistribution.toGraphDistribution();
                        previousNode.setDistribution(previousDistribution);
                        ((GraphDistribution)previousDistribution)
                                .addDistributionNode(level.getLocale().getDistribution());
                    }
                }
            } else {
                // create node and place it
                Distribution temp = bach.generateDistribution(input[i]);
                level.getGraphScope().addDistributionNode(temp);
                level.setLocale((DistributionNode) level.getGraphScope().getNewest());
                level.getLocale().addConfirmedDatum(input[i]);
            }
        }
        
        Scope origin = tracker.get(tracker.size()-1);
        tracker = new ArrayList();
        tracker.add(origin);
    }
    
    public GraphDistribution getMemory(){
        return memory;
    }
    
    public void sleep(){
        memory.goodNight();
    }
}
