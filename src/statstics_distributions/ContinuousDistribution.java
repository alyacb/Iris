
package statstics_distributions;

/**
 *
 * @author alyacarina
 */
public abstract class ContinuousDistribution extends Distribution {

    // Abstract superglass for grouping continuous distributions
    
    private static final double CONTINUOUS_DX = 0.0001;
    
    public ContinuousDistribution(String name, double[] parameters){
        super(name, parameters);
        setDX(CONTINUOUS_DX);
    }
    
}
