
package core;

//import graphs.DistributionNode;
import java.io.Serializable;
import statistics_analysis.DistributionGenerator;
import statistics_distributions.Distribution;
import statistics_distributions.GraphDistribution;

/**
 *
 * @author alyacarina
 */
public class Iris implements Serializable {
    private final GraphDistribution dist_memory;
    
    public Iris(){
        dist_memory = new GraphDistribution();
        /*dist_memory.getGraph().addDistributionNode(new GraphDistribution());
        dist_memory.getGraph().addDistributionNode(new GraphDistribution());
        DistributionNode x = (DistributionNode) dist_memory.getGraph().root.getNodeById(1);
        ((GraphDistribution)x.getDistribution()).getGraph().addDistributionNode(new GraphDistribution());
        ((GraphDistribution)x.getDistribution()).getGraph().addDistributionNode(new GraphDistribution());
        ((GraphDistribution)x.getDistribution()).getGraph().addDistributionNode(new GraphDistribution());
        ((GraphDistribution)x.getDistribution()).getGraph().addDistributionNode(new GraphDistribution());*/
    }
    
    // Convert string input to numbers- BASIC: chars to ints.
    public void input(String input){
        for(int i=0; i<input.length(); i++){
            double x = input.charAt(i);
            double fx = dist_memory.f(x);
            if(fx == 0){
                // need new Node, otherwise its been added
                Distribution distant = DistributionGenerator.generateBestDistribution(x);
                dist_memory.addDistributionNode(distant);
            }
        }
    }
    
    public GraphDistribution getMemory(){
        return dist_memory;
    }
}
