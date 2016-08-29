
package statistics;

/**
 *
 * @author alyacarina
 */
public class NormalDistribution extends ContinuousDistribution {
    private double variance;
    
    public NormalDistribution(double mean, double variance){
        super();
        this.mean = mean;
        this.variance = variance;
        this.name = "Normal Distribution";
        setLimits();
        num_params = 2;
    }
    
    private void setLimits(){
        this.lower_lim = mean - 5*Math.sqrt(variance);
        this.upper_lim = mean + 5*Math.sqrt(variance);
    }
    
    @Override 
    public void setMean(double m){
        super.setMean(m);
        setLimits();
    }
    
    public void setStandardDeviation(double x) {
        variance = x*x;
        setLimits();
    }

    @Override
    public double getStandardDeviation() {
        return Math.sqrt(variance);
    }

    @Override
    public double f(double x) {
        double exp = (x-mean);
        return Math.pow(Math.E, -exp*exp/(2*variance))/Math.sqrt(2*variance*Math.PI);
    }
    
    // for testing
    public static void main(String[] args){
        NormalDistribution n = new NormalDistribution(0, 1);
        System.out.println(n.F(1));
        System.out.println(n.F(2));
    }
    
}
