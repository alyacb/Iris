
package statistics;

/**
 *
 * @author alyacarina
 */
public class ExponentialDistribution extends ContinuousDistribution {
    
    public ExponentialDistribution(){
        mean = 1;
    }
    
    public ExponentialDistribution(double mean){
        this.mean = mean;
    }
    
    private void setLimits(){
        this.lower_lim = mean - 5*mean;
        this.upper_lim = mean + 5*mean;
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
        setMean(x);
    }

    @Override
    public double getStandardDeviation() {
        return mean;
    }

    @Override
    public double f(double x) {
        return 1/mean * Math.pow(Math.E, -x/mean);
    }
    
    @Override
    public double getCenter(){
        return mean;
    }
    
}
