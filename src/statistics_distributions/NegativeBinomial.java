
package statistics_distributions;

import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */
public class NegativeBinomial extends DiscreteDistribution {
    
    // This is the NegativeBinomial distribution class
    
    public NegativeBinomial(int k, double p) {
        super("Negative Binomial", new double[]{k, p}, Double.MAX_VALUE);
        validate();
    }

    @Override
    public double f_implementation(int x) {
        int k = (int)getParameter(0);
        double p = getParameter(1);
        return choose(x+k-1, x)*Math.pow(p, k)*Math.pow(1-p, x);
    }

    @Override
    public double getMean() {
        int k = (int)getParameter(0);
        double p = getParameter(1);
        return k*(1-p)/p;
    }

    @Override
    public double getVariance() {
        return getMean()/getParameter(1);
    }

    @Override
    protected double est_param_impl(int i, DataSet data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected final void validate() throws IllegalArgumentException {
           if(getParameter(1)<0 || getParameter(1)>1 || getParameter(0)<0){
            throwBadArgs();
        }
    }
    
}
