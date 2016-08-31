
package statistics_distributions;

/**
 *
 * @author alyacarina
 */
public class Uniform extends ContinuousDistribution {

    // This is a class implementing the Uniform distribution
    
    public Uniform(int a, int b) {
        super("Uniform", new double[]{Math.min(a, b), Math.max(a, b)});
        if(a==b){
            throwBadArgs();
        }
    }

    @Override
    public double getMean() {
        return (getParameter(0)+getParameter(1))/2;
    }

    @Override
    public double getVariance() {
        return Math.pow(getParameter(1)-getParameter(0),2)/12;
    }

    @Override
    public double f(double x) {
        if(x<getParameter(0) || x>getParameter(1)){
            return 0;
        }
        
        return 1/(getParameter(1)-getParameter(0));
    }
    
}
