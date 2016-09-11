
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class Poisson extends DiscreteDistribution {

    // this class implements the Poisson distribution
    
    private static double coefficient;
    
    public Poisson(double theta) {
        super("Poisson", new double[]{theta}, Double.MAX_VALUE);
        if(theta<=0){
            throwBadArgs();
        }
        coefficient = Math.exp(-theta);
    }

    @Override
    public double f_implementation(int x) {
        return coefficient*Math.pow(getParameter(0), x)/factorial(x);
    }

    @Override
    public double getMean() {
        return getParameter(0);
    }

    @Override
    public double getVariance() {
        return getMean();
    }

    @Override
    protected double est_param_impl(int i, DataSet data) {
        return data.getMean();
    }
    
}
