
package core;

import java.io.Serializable;
import statistics_analysis.DataSet;
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
    }
    
    // Convert string input to numbers- BASIC: chars to ints.
    public void input(String input){
        int addTo = 0;
        for(int i=0; i<input.length(); i++){
            double x = input.charAt(i);
            double fx = dist_memory.f(x);
            if(fx == 0){
                // need new Node, otherwise its been added
                Distribution distant = DistributionGenerator.generateBestDistribution(x);
                dist_memory.addDistributionNode(distant, addTo);
                dist_memory.getNewest().setData(new DataSet(new double[]{x}));
                addTo = dist_memory.getNewest().getId();
            } else {
                addTo = 0;
            }
        }
        dist_memory.clearTrack(); // input complete, start from scratch
        sleep();
    }
    
    public GraphDistribution getMemory(){
        return dist_memory;
    }
    
    public void sleep(){
        dist_memory.goodNight();
    }
}
