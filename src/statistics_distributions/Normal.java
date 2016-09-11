
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class Normal extends ContinuousDistribution {

    // This implements the Normal distribution
    
    private final double COEFFICIENT;
    
    public Normal(double mean, double variance) {
        super("Normal", new double[]{mean, variance});
        if(variance <= 0){
            throwBadArgs();
        }
        COEFFICIENT = Math.exp(-1/(2*variance))/Math.sqrt(2*Math.PI*variance);
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
        if(i == 1) {
            return data.getMean();
        } else {
            return data.getStandardDeviation();
        }
    }
    
}
