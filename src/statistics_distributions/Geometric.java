
package statistics_distributions;

/**
 *
 * @author alyacarina
 */
public class Geometric extends NegativeBinomial {
    
    // This class implements the Geometric distribution
    //    i.e. NegativeBinomial with k=1
    
    public Geometric(double p) {
        super(1, p);
    }
    
}
