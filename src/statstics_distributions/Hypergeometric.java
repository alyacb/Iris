
package statstics_distributions;

/**
 *
 * @author alyacarina
 */
public class Hypergeometric extends DiscreteDistribution {

    // This class implements the Hypergeormetric Distribution
    
    public Hypergeometric(int N, int r, int n) {
        super("Hypergeometric", new double[]{N, r, n}, Math.min(r, n));
        if(N<=r || N<=n){
            throwBadArgs();
        }
    }

    @Override
    public double f_implementation(int x) {
        int N = (int)getParameter(0);
        int r = (int)getParameter(1);
        int n = (int)getParameter(2);
        return choose(r, x)*choose(N-r, n-x)/choose(N, n);
    }

    @Override
    public double getMean() {
        return getParameter(2)*getParameter(1)/getParameter(0);
    }

    @Override
    public double getVariance() {
        int N = (int)getParameter(0);
        int r = (int)getParameter(1);
        int n = (int)getParameter(2);
        return getMean()*(1-r/N)*(N-n)/(n-1);
    }
    
}
