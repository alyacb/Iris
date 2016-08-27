package statistics;

/**
 *
 * @author alyacarina
 */
public class ChiSquared extends ContinuousDistribution {

    private static double DENOMINATOR;
    
    public ChiSquared(int degrees_of_freedom) {
        name  = "Chi Squared";
        
        if (degrees_of_freedom < 0) {
            throw new IllegalStateException("ChiSquared must have positive DOF");
        }
        this.mean = degrees_of_freedom;
        lower_lim = 0;
        upper_lim = 10;
        initialize();
        num_params = 1;
    }

    private void initialize(){
        DENOMINATOR = Math.pow(2, mean/2) * gamma(mean/2);
    }
    
    // factorial
    private double gamma(double x) {
        double n = 1;
        for(int i=2; i<x; i++){
            n*=i;
        }
        return n;
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
    public double f(double x) {
        if (x < 0) {
            return 0;
        } else {
            double num = Math.pow(x, mean/2 - 1) * Math.exp(-x/2);
            return num/DENOMINATOR;
        }
    }
    
    // Testing
    public static void main(String[] args){
        ChiSquared x = new ChiSquared(5);
        System.out.println(x.f(1));
        System.out.println(x.F(0.554));
        x.setMean(10);
        System.out.println(x.f(1));
        System.out.println(x.F(9.342));
        x.setMean(100);
        System.out.println(x.F(67.328));
    }

}
