
package statistics_distributions;

/**
 *
 * @author alyacarina
 */
public class Bernoulli extends Binomial {

    // This class implements the Bernoulli Distribution
    //   i.e. Binomial with n=1
    
    public Bernoulli(double p) {
        super(1, p);
    }
    
}
