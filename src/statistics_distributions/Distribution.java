
package statistics_distributions;

import java.io.Serializable;
import statistics_analysis.DataSet;

/**
 *
 * @author alyacarina
 */

public abstract class Distribution implements Serializable {
    
    // Abstract superclass managing statistical distributions.
    //    Each concrete subclass has a unique implementation of CDF, PDF
    
    private final double parameters[];
    private final String name;
    private double DX;
    
    public Distribution(String name, double[] parameters){
        this.name = name;
        this.parameters = parameters;
    }
    
    // error methods
    protected final void throwBadArgs() throws IllegalArgumentException {
        throw new IllegalArgumentException(name + ": recieved an invalid argument.");
    }
    
    // getters, self-explanatory titles.
    public abstract double getMean();
    public double getStandardDeviation(){
        return Math.sqrt(this.getVariance());
    }
    public abstract double getVariance();
    
    public final String getName(){
        return name;
    }
    public final int getNumberOfParameters(){
        return parameters.length;
    }
    public final double getParameter(int i){
        return parameters[i];
    }
    public final double getDX(){
        return DX;
    }

    @Override 
    public final String toString(){
        String s = name;
        s+='(';
        for(int n=0; n<parameters.length-1; n++){
            s+=parameters[n]+", ";
        }
        if(parameters.length>0){
            s+=parameters[parameters.length-1];
        }
        s+=')';
        return s;
    }
    
    // method for setting integration interval size
    protected final void setDX(double dx){
        DX = dx;
    }
    
    public final void setParameter(int i, double value){
        parameters[i] = value;
        validate();
    }
    
    // method for integration of f(x)
    protected final double integrate_f(double a, double b){
        double lower_limit = Math.min(a, b);
        double upper_limit = Math.max(a, b);
        double result = 0;
        for(double x = lower_limit; x<=upper_limit; x+=DX){
            result+=f(x)*DX;
        }
        return result;
    }
    
    // Methods used by several subclasses in PDF
    protected final long factorial(long n){
        long result = 1;
        for(int i=2; i<=n; i++){
            result = result *i;
        }
        return result;
    }
    
    protected final long choose(int a, int b){
        long maxi = Math.max(a, b);
        long mini = Math.min(a, b);
        return factorial(maxi)/(factorial(mini)*factorial(maxi-mini));
    }
    
    protected final double gamma(double t){
        // Euler
        double result = 1/t;
        for(int n=1; n<100000; n++){
            double num = Math.pow(1+1/(double)n, t);
            double denom = (1+t/(double)n);
            result*=num;
            result/=denom;
        }
        return result;
    }
    
    // Probability Density Function
    public abstract double f(double x);
    
    // Cumulative Distribution Function
    public double F(double x){
        return integrate_f(getMean() - 5*getStandardDeviation(), x);
    }

    // Estimate ith parameter in a distribution, based on dataset's values
    //    (assuming data is drawn from this distribution)
    public final double estimateParameter(int i, DataSet data){
        if(i<0 || i>getNumberOfParameters()) {
            throwBadArgs();
            return -1;
        }
        return est_param_impl(i, data);
    }
    
    // implementation of above, unique to each distribution
    protected abstract double est_param_impl(int i, DataSet data);
    
    // validates the arguments of the distribution
    protected abstract void validate() throws IllegalArgumentException;
}
