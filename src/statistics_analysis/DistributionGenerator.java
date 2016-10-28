
package statistics_analysis;

import graphs.DistributionManager;
import graphs.DistributionNode;
import graphs.MemoryNode;
import java.util.ArrayList;
import statistics_distributions.BoringDistribution;
import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
public class DistributionGenerator {

    // A class that generates the most appropriate distribution for a data-set
    //   Using an existing graph of distributions
    
    private final DistributionManager boss;
    private final PValueGenerator engine;
    private Distribution current_max;
    private double max_p_value;
    
    public DistributionGenerator(DataSet dataSet, DistributionManager boss) {
        this.boss = boss;
        engine = new PValueGenerator(dataSet);
    }
    
    public static Distribution generateBestDistribution(double lonely){
        return new BoringDistribution(lonely);
    }
    
    private void generateBestDistribution(DistributionNode node, 
                                          ArrayList<Integer> visited){
        visited.add(node.getId());
        for(MemoryNode mn: node.getNeighbors()){
            if(visited.contains(mn.getId())) continue;
            
            double p_value = engine.getPValue(node.getDistribution(), node.getPreferredBinSize());
            
            if(p_value > max_p_value){
                max_p_value = p_value;
                current_max = node.getDistribution();
            }
            
            generateBestDistribution((DistributionNode) mn, visited);
        }
    }
    
    // Generates a distribution that best fits the dataset, from among known ones
    public Distribution generateBestDistribution(){
        max_p_value = 0;
        current_max = null;
        generateBestDistribution((DistributionNode)boss.root, new ArrayList());
        return current_max;
    }
    
}
