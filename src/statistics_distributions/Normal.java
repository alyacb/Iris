
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class Normal extends ContinuousDistribution {

    // This implements the Normal distribution
    
    private double COEFFICIENT;
    
    public Normal(double mean, double variance) {
        super("Normal", new double[]{mean, variance});
        validate();
    }

    @Override
    public double getMean() {
        return getParameter(0);
    }

    @Override
    public double getVariance() {
        return getParameter(1);
    }

    @Override
    public double f(double x) {
        return COEFFICIENT*Math.exp(-Math.pow(x-getMean(), 2));
    }

    @Override
    public double est_param_impl(int i, DataSet data) {
        if(i == 0) {
            return data.getMean();
        } else {
            return data.getStandardDeviation();
        }
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
        if(getParameter(1) <= 0){
            throwBadArgs();
        }
        COEFFICIENT = Math.exp(-1/(2*getParameter(1)))/Math.sqrt(2*Math.PI*getParameter(1));
    }
    
}
