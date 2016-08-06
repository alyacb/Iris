
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
        setLimits();
    }
    
    private void setLimits(){
        this.lower_lim = mean - 5*Math.sqrt(variance);
        this.upper_lim = mean + 5*Math.sqrt(variance);
    }
    
    @Override 
    public String getName(){
        return "Normal Distribution";
    }
    
    @Override 
    public void setMean(double m){
        super.setMean(m);
        setLimits();
    }
    
    @Override
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

    @Override
    public double getCenter() {
        return 0;
    }
    
}
