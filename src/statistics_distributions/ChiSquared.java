
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class ChiSquared extends ContinuousDistribution {
    
    // This class implements the Chi-Squared distribution

    private double COEFFICIENT;
    
    public ChiSquared(int k) {
        super("Chi-Squared", new double[]{k});
        validate();
    }

    @Override
    public double getMean() {
        return getParameter(0);
    }

    @Override
    public double getVariance() {
        return 2*getParameter(0);
    }

    @Override
    public double f(double x) {
        if(x<=0){
            return 0;
        }
        return COEFFICIENT*Math.pow(x, (getParameter(0)/2)-1)*Math.exp(-x/2);
    }

    @Override
    public double est_param_impl(int i, DataSet data) {
        return (int)data.getMean();
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
        if(getParameter(0)<=0 || getParameter(0)!=(int)getParameter(0)){
            throwBadArgs();
        }
        COEFFICIENT = 1/(Math.pow(2, (double)getParameter(0)/2)*gamma((double)getParameter(0)/2));
    }
}
