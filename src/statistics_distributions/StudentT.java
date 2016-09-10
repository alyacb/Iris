
package statistics_distributions;

/**
 *
 * @author alyacarina
 */
public class StudentT extends ContinuousDistribution {
    
    // implements Student T's distribution

    private final double COEFFICIENT;
    
    public StudentT(int k) {
        super("Student T's", new double[]{k});
        if(k<=0){
            throwBadArgs();
        }
        double num = gamma((double)(k+1)/2);
        double denom = (Math.sqrt(Math.PI*k)*gamma(((double)k)/2));
        COEFFICIENT = num/denom;
    }

    @Override
    public double getMean() {
        return 0; // Note that its undefined for k=1, irrelevant to my calculations (for now)
    }

    @Override
    public double getVariance() {
        return getParameter(0)/(getParameter(0)-2);
    }

    @Override
    public double f(double x) {
        return COEFFICIENT*Math.pow(1+x*x/getParameter(0), -(getParameter(0)+1)/2);
    }
}
