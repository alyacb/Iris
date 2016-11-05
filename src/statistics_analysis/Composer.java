
package statistics_analysis;

import core.Iris;
import statistics_distributions.BoringDistribution;
import statistics_distributions.Distribution;

/**
 *
 * @author alyacarina
 */
public class Composer {
    
    private final Iris owner;
    
    public Composer(Iris owner){
        this.owner = owner;
    }

    public Distribution generateDistribution(double d) {
        return new BoringDistribution(d);
    }
    
}
