
package statistics;

/**
 *
 * @author alyacarina
 */
public abstract class ContinuousDistribution {
    protected double mean;
    protected double dx = 0.001;
    protected double lower_lim = 0;
    protected double upper_lim = 1;
    
    public String getName(){
        return "";
    }
    
    public void setLowerLimit(double l){
        lower_lim = l;
    }
    
    public double getLowerLimit(){
        return lower_lim;
    }
    
    public double getUpperLimit(){
        return upper_lim;
    }
    
    public void setDx(double d){
        dx = d;
    }
    
    public double getDx(){
        return dx;
    }
    
    public void setMean(double x) {
        mean = x;
    }
    
    public double getMean(){
        return mean;
    }

    public abstract void setStandardDeviation(double x);
    public abstract double getStandardDeviation();
    
    // probability
    public abstract double f(double x);
    
    // cumulative distribution
    public double F(double x){
        double val = 0;
        for(double d = lower_lim; d<x; d+=dx){
            val+=f(d)*dx;
        }
        return val;
    }
}
