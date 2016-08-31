
package statistics_distributions;

/**
 *
 * @author alyacarina
 */
public class ChiSquared extends ContinuousDistribution {
    
    // This class implements the Chi-Squared distribution

    private final double COEFFICIENT;
    
    public ChiSquared(int k) {
        super("Chi-Squared", new double[]{k});
        if(k<=0){
            throwBadArgs();
        }
        COEFFICIENT = 1/(Math.pow(2, k/2)*factorial(k/2-1));
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
        return COEFFICIENT*Math.pow(x, getParameter(0)/2-1)*Math.exp(-x/2);
    }
    
}
