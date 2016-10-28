
package core;

import statistics_analysis.DistributionGenerator;
import statistics_distributions.Distribution;
import statistics_distributions.GraphDistribution;

/**
 *
 * @author alyacarina
 */
public class Iris {
    private GraphDistribution dist_memory;
    
    public Iris(){
        dist_memory = new GraphDistribution();
    }
    
    // Convert string input to numbers- BASIC: chars to ints.
    public void input(String input){
        int last_id = 0;
        for(int i=0; i<input.length(); i++){
            double x = input.charAt(i);
            double fx = dist_memory.f(x);
            if(fx == 0){
                // need new Node, otherwise its been added
                Distribution distant = DistributionGenerator.generateBestDistribution(x);
                dist_memory.getGraph().addDistributionNode(distant, last_id);
                last_id = dist_memory.getGraph().getNumberOfNodes();
            }
        }
    }
    
    public GraphDistribution getMemory(){
        return dist_memory;
    }
}
