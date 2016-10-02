
package graphs;

import java.util.ArrayList;
import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
public class DistributionNode extends MemoryNode {
    
    public DistributionNode(Distribution d, ArrayList<MemoryNode> neighbors, int id) {
        super(neighbors, id);
    }
    
}
