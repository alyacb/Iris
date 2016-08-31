
package statistics_distributions;

/**
 *
 * @author alyacarina
 */
public class NegativeBinomial extends DiscreteDistribution {
    
    // This is the NegativeBinomial distribution class
    
    public NegativeBinomial(int k, double p) {
        super("Negative Binomial", new double[]{k, p}, Double.MAX_VALUE);
        if(p<0 || p>1){
            throwBadArgs();
        }
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
    
}
