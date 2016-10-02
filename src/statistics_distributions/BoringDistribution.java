
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */

// A boolean distribution- returns one if the value matches exactly the one provided
//    in its constructor (the sole parameter) and zero otherwise

public class BoringDistribution extends Distribution {

    public BoringDistribution(double value){
        super("Boring Distribution", new double[]{value});
        validate();
    }
    
    @Override
    public double getMean() {
        return getParameter(0);
    }

    @Override
    public double getVariance() {
        return 0;
    }

    @Override
    public double f(double x) {
        if(x == getParameter(0)){
            return 1;
        }
        return 0;
    }
    
    @Override
    public double F(double x){
        if(x<getParameter(0)){
            return 0;
        }
        return 1;
    }

    @Override
    protected double est_param_impl(int i, DataSet data) {
        if(data.getVariance()!=0) throwBadArgs();
        return data.getMean();
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
        // never invalid
    }
    
}
