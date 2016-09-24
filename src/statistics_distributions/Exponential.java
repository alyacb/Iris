
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class Exponential extends ContinuousDistribution {

    // This implements the Exponential distribution
    
    public Exponential(double theta) {
        super("Exponential", new double[]{theta});
        validate();
    }

    @Override
    public double getMean() {
        return getParameter(0);
    }

    @Override
    public double getVariance() {
        return getParameter(0)*getParameter(0);
    }

    @Override
    public double f(double x) {
        if(x <= 0){
            return 0;
        }
        return Math.exp(-x/getParameter(0))/getParameter(0);
    }

    @Override
    public double est_param_impl(int i, DataSet data) {
        return data.getMean();
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
        if(getParameter(0) <= 0){
            throwBadArgs();
        }
    }
    
}
