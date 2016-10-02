
package graphs;

import java.util.ArrayList;
import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
public class DistibutionNode extends MemoryNode {
    
    public DistibutionNode(Distribution d, ArrayList<MemoryNode> neighbors, int id) {
        super(neighbors, id);
    }
    
}
