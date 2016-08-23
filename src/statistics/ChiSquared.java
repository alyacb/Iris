package statistics;

/**
 *
 * @author alyacarina
 */
public class ChiSquared extends ContinuousDistribution {

    private static double DENOMINATOR;
    
    public ChiSquared(int degrees_of_freedom) {
        if (degrees_of_freedom < 0) {
            throw new IllegalStateException("ChiSquared must have positive DOF");
        }
        this.mean = degrees_of_freedom;
        lower_lim = 0;
        upper_lim = 10;
        initialize();
    }

    private void initialize(){
        DENOMINATOR = Math.pow(2, mean/2) * gamma(mean/2);
    }
    
    // factorial
    private double gamma(double x) {
        if (x <= 1) {
            return 1;
        } else {
            return gamma(x - 1) * x;
        }
    }
    
    @Override
    public void setMean(double x){
        super.setMean(x);
        initialize();
    }

    @Override
    public void setStandardDeviation(double x) {
        mean = Math.sqrt(x / 2);
        initialize();
    }

    @Override
    public double getStandardDeviation() {
        return Math.sqrt(2 * mean);
    }

    @Override
    public double getCenter() {
        return mean;
    }

    @Override
    public double f(double x) {
        if (x < 0) {
            return 0;
        } else {
            double num = Math.pow(x, mean/2 - 1) * Math.exp(-x/2);
            return num/DENOMINATOR;
        }
    }

}
