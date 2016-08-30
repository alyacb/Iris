
package statstics_distributions;

/**
 *
 * @author alyacarina
 */
public class Binomial extends DiscreteDistribution {
    
    // This is the Binomial Distribution class
    //   i.e. special case of multinomial with only 2 parameters
    
    public Binomial(int n, double p){
        super("Binomial", new double[]{n, p}, n);
        if(p<0 || p>1){
            throwBadArgs();
        }
    }
    
    @Override
    public double getMean() {
        return getParameter(0)*getParameter(1);
    }

    @Override
    public double getVariance() {
        return getMean()*(1-getParameter(1));
    }
    
    @Override
    public double f_implementation(int x) {
        int n = (int) getParameter(0);
        double p = getParameter(1);
        return choose(n, x)*Math.pow(p, x)*Math.pow(1-p, n-x);
    }
    
}
