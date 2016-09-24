
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class Hypergeometric extends DiscreteDistribution {

    // This class implements the Hypergeormetric Distribution
    
    public Hypergeometric(int N, int r, int n) {
        super("Hypergeometric", new double[]{N, r, n}, Math.min(r, n));
        validate();
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

    @Override
    protected double est_param_impl(int i, DataSet data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
        int N = (int)getParameter(0);
        int r = (int)getParameter(1);
        int n = (int)getParameter(2);
        if(N<=r || N<=n){
            throwBadArgs();
        }
    }

}
